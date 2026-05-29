package re1kur.app.dto.websocket;

public record ChatMessageDto(
        Long id,
        String room,
        ChatPrincipal sender,
        String text,
        String timestamp
) {
}
