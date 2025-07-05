package re1kur.app.core.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserPayload(
        @Email(message = "Invalid email.")
        String email,
        @NotBlank
        @NotNull
        @Size(min = 6, max = 128)
        String password,
        @Size(min = 6, max = 64)
        String fullName
) {
}
