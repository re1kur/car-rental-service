package re1kur.app.api.dto;

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
