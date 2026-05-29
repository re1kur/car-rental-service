package re1kur.app.websocket.dto;

import re1kur.app.websocket.ChatPrincipal;

public record ChatMessageDto(
        Long id,
        String room,
        ChatPrincipal sender,
        String text,
        String timestamp
) {
}
