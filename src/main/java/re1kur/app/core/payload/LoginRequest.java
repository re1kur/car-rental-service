package re1kur.app.core.payload;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record LoginRequest(
        @Email(message = "Invalid email.")
        String email,
        @NotNull
        @NotBlank
        String password
) {
}
