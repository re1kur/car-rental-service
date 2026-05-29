package re1kur.app.dto.websocket;

public record ChatPrincipal(
        String userId,
        String displayName,
        boolean guest
) {
}
