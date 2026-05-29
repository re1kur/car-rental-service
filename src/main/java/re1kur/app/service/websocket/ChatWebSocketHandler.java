package re1kur.app.service.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import re1kur.app.dto.websocket.*;
import re1kur.app.service.chat.ChatHistoryService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final int MAX_TEXT_LENGTH = 2000;

    private final ChatRegistry registry;
    private final ChatHistoryService history;
    private final ObjectMapper mapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        ChatPrincipal attached = (ChatPrincipal) session.getAttributes()
                .get(HandshakeAuthInterceptor.PRINCIPAL_ATTRIBUTE);
        ChatPrincipal principal = attached != null ? attached : guest(session.getId());

        WebSocketSession safe = new ConcurrentWebSocketSessionDecorator(session, 5000, 64 * 1024);
        String ip = session.getRemoteAddress() != null
                ? session.getRemoteAddress().getAddress().getHostAddress() : "unknown";

        ChatUserSession chatSession = new ChatUserSession(safe, principal, ip, Instant.now());
        registry.register(chatSession);

        log.info("WS connected: socketId=[{}] user=[{}] guest=[{}] ip=[{}] at=[{}]",
                chatSession.socketId(), principal.displayName(), principal.guest(), ip, chatSession.connectedAt());

        send(chatSession, "connected", Map.of(
                "socketId", chatSession.socketId(),
                "user", principal,
                "rooms", roomIds()));
        send(chatSession, "room_counts", Map.of("counts", registry.counts()));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        ChatUserSession chatSession = registry.get(session.getId());
        if (chatSession == null) {
            return;
        }

        JsonNode root;
        try {
            root = mapper.readTree(message.getPayload());
        } catch (Exception e) {
            send(chatSession, "error", Map.of("message", "Malformed JSON."));
            return;
        }

        String type = root.path("type").asText(null);
        JsonNode data = root.path("data");
        if (type == null) {
            send(chatSession, "error", Map.of("message", "Missing event type."));
            return;
        }

        try {
            switch (type) {
                case "join_room" -> onJoin(chatSession, data);
                case "leave_room" -> onLeave(chatSession, data);
                case "send_message" -> onSend(chatSession, data);
                case "typing" -> onTyping(chatSession, data);
                case "get_online_users" -> onGetOnlineUsers(chatSession, data);
                default -> send(chatSession, "error", Map.of("message", "Unknown event: " + type));
            }
        } catch (Exception e) {
            log.warn("WS event [{}] failed for socketId=[{}]: {}", type, chatSession.socketId(), e.getMessage());
            send(chatSession, "error", Map.of("message", "Failed to process event."));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        ChatUserSession chatSession = registry.get(session.getId());
        if (chatSession == null) {
            return;
        }
        List<String> rooms = new ArrayList<>(chatSession.rooms());
        registry.unregister(chatSession.socketId());

        for (String room : rooms) {
            broadcast(room, "user_left", Map.of("room", room, "user", chatSession.principal()));
            broadcast(room, "online_users", Map.of("room", room, "users", registry.onlineUsers(room)));
        }
        broadcastRoomCounts();
        log.info("WS disconnected: socketId=[{}] user=[{}] status=[{}]",
                chatSession.socketId(), chatSession.principal().displayName(), status);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.warn("WS transport error on socketId=[{}]: {}", session.getId(), exception.getMessage());
    }

    @PreDestroy
    public void shutdown() {
        log.info("WS graceful shutdown: closing [{}] session(s)", registry.all().size());
        for (ChatUserSession chatSession : registry.all()) {
            try {
                chatSession.session().close(CloseStatus.GOING_AWAY);
            } catch (Exception ignored) {
                // shutting down anyway
            }
        }
    }

    // ----- event handlers -----

    private void onJoin(ChatUserSession chatSession, JsonNode data) {
        String room = data.path("room").asText(null);
        if (notValidRoom(chatSession, room)) {
            return;
        }
        registry.joinRoom(chatSession.socketId(), room);

        send(chatSession, "history", Map.of("room", room, "messages", history.recent(room)));
        broadcastExcept(room, "user_joined", Map.of("room", room, "user", chatSession.principal()), chatSession.socketId());
        broadcast(room, "online_users", Map.of("room", room, "users", registry.onlineUsers(room)));
        broadcastRoomCounts();
    }

    private void onLeave(ChatUserSession chatSession, JsonNode data) {
        String room = data.path("room").asText(null);
        if (room == null) {
            send(chatSession, "error", Map.of("message", "Missing room."));
            return;
        }
        registry.leaveRoom(chatSession.socketId(), room);
        broadcast(room, "user_left", Map.of("room", room, "user", chatSession.principal()));
        broadcast(room, "online_users", Map.of("room", room, "users", registry.onlineUsers(room)));
        broadcastRoomCounts();
    }

    private void onSend(ChatUserSession chatSession, JsonNode data) {
        String room = data.path("room").asText(null);
        String text = data.path("text").asText(null);
        if (notValidRoom(chatSession, room)) {
            return;
        }
        if (!chatSession.rooms().contains(room)) {
            send(chatSession, "error", Map.of("message", "Join the room before sending messages."));
            return;
        }
        if (text == null || text.isBlank()) {
            send(chatSession, "error", Map.of("message", "Message text is empty."));
            return;
        }
        text = text.strip();
        if (text.length() > MAX_TEXT_LENGTH) {
            text = text.substring(0, MAX_TEXT_LENGTH);
        }
        ChatMessageDto dto = history.save(room, chatSession.principal(), text);
        broadcast(room, "message", dto);
        broadcastAll("room_activity", Map.of("room", room));
    }

    private void onTyping(ChatUserSession chatSession, JsonNode data) {
        String room = data.path("room").asText(null);
        boolean isTyping = data.path("isTyping").asBoolean(false);
        if (notValidRoom(chatSession, room)) {
            return;
        }
        broadcastExcept(room, "typing_status",
                Map.of("room", room, "user", chatSession.principal(), "isTyping", isTyping),
                chatSession.socketId());
    }

    private void onGetOnlineUsers(ChatUserSession chatSession, JsonNode data) {
        String room = data.path("room").asText(null);
        if (notValidRoom(chatSession, room)) {
            return;
        }
        send(chatSession, "online_users", Map.of("room", room, "users", registry.onlineUsers(room)));
    }

    // ----- helpers -----

    private boolean notValidRoom(ChatUserSession chatSession, String room) {
        if (room == null || !ChatRoom.exists(room)) {
            send(chatSession, "error", Map.of("message", "Room does not exist: " + room));
            return true;
        }
        return false;
    }

    private void broadcastRoomCounts() {
        broadcastAll("room_counts", Map.of("counts", registry.counts()));
    }

    private void broadcastAll(String type, Object data) {
        for (ChatUserSession session : registry.all()) {
            send(session, type, data);
        }
    }

    private void broadcast(String room, String type, Object data) {
        broadcastExcept(room, type, data, null);
    }

    private void broadcastExcept(String room, String type, Object data, String exceptSocketId) {
        for (ChatUserSession member : registry.membersOf(room)) {
            if (exceptSocketId != null && member.socketId().equals(exceptSocketId)) {
                continue;
            }
            send(member, type, data);
        }
    }

    private void send(ChatUserSession chatSession, String type, Object data) {
        try {
            String json = mapper.writeValueAsString(new WsEvent(type, data));
            chatSession.session().sendMessage(new TextMessage(json));
        } catch (Exception e) {
            log.warn("WS send [{}] failed to socketId=[{}]: {}", type, chatSession.socketId(), e.getMessage());
        }
    }

    private ChatPrincipal guest(String socketId) {
        String shortId = socketId.length() > 5 ? socketId.substring(0, 5) : socketId;
        return new ChatPrincipal("guest:" + socketId, "Guest-" + shortId, true);
    }

    private List<String> roomIds() {
        return Arrays.stream(ChatRoom.values()).map(ChatRoom::id).toList();
    }
}
