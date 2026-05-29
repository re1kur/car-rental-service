package re1kur.app.dto.websocket;

public record NotificationDto(
        String title,
        String body,
        String url,
        String timestamp
) {
}
