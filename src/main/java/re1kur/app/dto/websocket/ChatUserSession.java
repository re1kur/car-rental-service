package re1kur.app.dto.websocket;

import org.springframework.web.socket.WebSocketSession;

import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatUserSession {

    private final WebSocketSession session;
    private final ChatPrincipal principal;
    private final String remoteAddress;
    private final Instant connectedAt;
    private final Set<String> rooms = ConcurrentHashMap.newKeySet();

    public ChatUserSession(WebSocketSession session, ChatPrincipal principal, String remoteAddress, Instant connectedAt) {
        this.session = session;
        this.principal = principal;
        this.remoteAddress = remoteAddress;
        this.connectedAt = connectedAt;
    }

    public String socketId() {
        return session.getId();
    }

    public WebSocketSession session() {
        return session;
    }

    public ChatPrincipal principal() {
        return principal;
    }

    public String remoteAddress() {
        return remoteAddress;
    }

    public Instant connectedAt() {
        return connectedAt;
    }

    public Set<String> rooms() {
        return rooms;
    }
}
