package re1kur.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NotificationRequest(
        @NotBlank(message = "Title is required.")
        @Size(max = 120, message = "Title is too long.")
        String title,

        @NotBlank(message = "Body is required.")
        @Size(max = 500, message = "Body is too long.")
        String body
) {
}
