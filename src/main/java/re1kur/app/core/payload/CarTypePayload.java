package re1kur.app.core.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CarTypePayload(
        @NotBlank(message = "Name cannot be empty or contain backspaces.")
        @Size(min = 3, max = 16, message = "Name must be between 3 and 16 characters long.")
        String name
) {

}
