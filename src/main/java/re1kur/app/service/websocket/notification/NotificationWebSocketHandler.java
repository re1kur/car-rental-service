package re1kur.app.service.websocket.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import re1kur.app.dto.websocket.WsEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationWebSocketHandler extends TextWebSocketHandler {

    private final NotificationRegistry registry;
    private final ObjectMapper mapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("NOTIFY ws connected: socketId=[{}]", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String type;
        try {
            type = mapper.readTree(message.getPayload()).path("type").asText(null);
        } catch (Exception e) {
            return;
        }
        if ("subscribe".equals(type)) {
            registry.subscribe(session);
            reply(session, "subscribed");
            log.info("NOTIFY subscribe: socketId=[{}]", session.getId());
        } else if ("unsubscribe".equals(type)) {
            registry.unsubscribe(session.getId());
            reply(session, "unsubscribed");
            log.info("NOTIFY unsubscribe: socketId=[{}]", session.getId());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        registry.unsubscribe(session.getId());
    }

    private void reply(WebSocketSession session, String type) {
        try {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(new WsEvent(type, null))));
        } catch (Exception ignored) {
            // best-effort confirmation
        }
    }
}
