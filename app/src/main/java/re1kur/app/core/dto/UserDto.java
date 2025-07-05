package re1kur.app.core.dto;

import lombok.*;

@Builder
public record UserDto(
        String id,
        String email,
        String fullName,
        boolean isEmailVerified
) {
}
