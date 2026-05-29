package re1kur.app.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PushSubscriptionRequest(
        @NotBlank(message = "FCM token is required.")
        String token
) {
}
