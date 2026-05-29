package re1kur.app.dto.websocket;

public record WsEvent(
        String type,
        Object data
) {
}
