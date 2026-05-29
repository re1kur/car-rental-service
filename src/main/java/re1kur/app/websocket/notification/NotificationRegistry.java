package re1kur.app.websocket.notification;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NotificationRegistry {

    private final Map<String, WebSocketSession> subscribers = new ConcurrentHashMap<>();

    public void subscribe(WebSocketSession session) {
        subscribers.put(session.getId(), new ConcurrentWebSocketSessionDecorator(session, 5000, 64 * 1024));
    }

    public void unsubscribe(String socketId) {
        subscribers.remove(socketId);
    }

    public Collection<WebSocketSession> subscribers() {
        return subscribers.values();
    }
}
