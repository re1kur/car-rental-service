package re1kur.app.core.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record MakePayload(
        @NotBlank(message = "Name cannot be empty or contain backspaces.")
        @Size(message = "Name have to be between 64 and 3 characters long.", min = 3, max = 64)
        String name,
        @NotBlank(message = "Country cannot be empty or contain backspaces.")
        @Size(message = "Country have to be between 32 and 3 characters long.", min = 3, max = 32)
        String country,
        String description,
        String titleImageUrl,
        MultipartFile image
) {
}
