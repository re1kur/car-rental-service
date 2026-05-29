package re1kur.app.dto.response;

import java.util.List;

public record MeResponse(
        String subject,
        String username,
        String email,
        String name,
        List<String> roles,
        boolean admin
) {
}
