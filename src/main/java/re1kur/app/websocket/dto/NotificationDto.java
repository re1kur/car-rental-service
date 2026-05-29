package re1kur.app.websocket.dto;

public record NotificationDto(
        String title,
        String body,
        String url,
        String timestamp
) {
}
