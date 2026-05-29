package re1kur.app.websocket;

public record ChatPrincipal(
        String userId,
        String displayName,
        boolean guest
) {
}
