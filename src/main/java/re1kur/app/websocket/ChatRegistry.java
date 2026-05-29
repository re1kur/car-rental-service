package re1kur.app.websocket;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatRegistry {

    private final Map<String, ChatUserSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> roomMembers = new ConcurrentHashMap<>();

    public void register(ChatUserSession session) {
        sessions.put(session.socketId(), session);
    }

    public ChatUserSession get(String socketId) {
        return sessions.get(socketId);
    }

    public Collection<ChatUserSession> all() {
        return sessions.values();
    }

    public void unregister(String socketId) {
        ChatUserSession session = sessions.remove(socketId);
        if (session != null) {
            session.rooms().forEach(room -> leaveRoom(socketId, room));
        }
    }

    public void joinRoom(String socketId, String roomId) {
        ChatUserSession session = sessions.get(socketId);
        if (session == null) {
            return;
        }
        session.rooms().add(roomId);
        roomMembers.computeIfAbsent(roomId, key -> ConcurrentHashMap.newKeySet()).add(socketId);
    }

    public void leaveRoom(String socketId, String roomId) {
        ChatUserSession session = sessions.get(socketId);
        if (session != null) {
            session.rooms().remove(roomId);
        }
        Set<String> members = roomMembers.get(roomId);
        if (members != null) {
            members.remove(socketId);
        }
    }

    public List<ChatUserSession> membersOf(String roomId) {
        return roomMembers.getOrDefault(roomId, Set.of()).stream()
                .map(sessions::get)
                .filter(Objects::nonNull)
                .toList();
    }

    public List<ChatPrincipal> onlineUsers(String roomId) {
        Map<String, ChatPrincipal> byUser = new LinkedHashMap<>();
        for (ChatUserSession session : membersOf(roomId)) {
            byUser.putIfAbsent(session.principal().userId(), session.principal());
        }
        return new ArrayList<>(byUser.values());
    }
}
