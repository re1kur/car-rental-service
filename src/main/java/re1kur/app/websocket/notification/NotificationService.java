package re1kur.app.websocket.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import re1kur.app.websocket.dto.NotificationDto;
import re1kur.app.websocket.dto.WsEvent;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRegistry registry;
    private final ObjectMapper mapper;

    public int broadcast(String title, String body, String url) {
        NotificationDto dto = new NotificationDto(title, body, url, Instant.now().toString());
        String json;
        try {
            json = mapper.writeValueAsString(new WsEvent("notification", dto));
        } catch (Exception e) {
            log.warn("Failed to serialize notification: {}", e.getMessage());
            return 0;
        }

        int sent = 0;
        for (WebSocketSession session : registry.subscribers()) {
            try {
                session.sendMessage(new TextMessage(json));
                sent++;
            } catch (Exception e) {
                log.warn("Notification send failed to socketId=[{}]: {}", session.getId(), e.getMessage());
            }
        }
        log.info("NOTIFY broadcast [{}] -> [{}] subscriber(s)", title, sent);
        return sent;
    }
}
