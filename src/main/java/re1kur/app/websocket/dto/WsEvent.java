package re1kur.app.websocket.dto;

public record WsEvent(
        String type,
        Object data
) {
}
