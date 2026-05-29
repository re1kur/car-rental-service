package re1kur.app.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddCartItemRequest(
        @NotNull(message = "carId is required.")
        Integer carId,

        // quantity = rental days (default 1 if omitted)
        @Min(value = 1, message = "quantity must be at least 1.")
        @Max(value = 365, message = "quantity cannot exceed 365 days.")
        Integer quantity
) {
}
