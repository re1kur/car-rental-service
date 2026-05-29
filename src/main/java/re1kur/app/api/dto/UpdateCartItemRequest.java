package re1kur.app.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateCartItemRequest(
        // quantity = rental days
        @NotNull(message = "quantity is required.")
        @Min(value = 1, message = "quantity must be at least 1.")
        @Max(value = 365, message = "quantity cannot exceed 365 days.")
        Integer quantity
) {
}
