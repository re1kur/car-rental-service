package re1kur.app.core.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record EnginePayload(
        @NotBlank(message = "Name cannot be empty or contain backspaces.")
        @NotNull(message = "Name is required.")
        @Size(min = 3, max = 32, message = "Name must be between 3 and 16 characters long.")
        String name) {
}
