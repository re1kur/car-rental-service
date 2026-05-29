package re1kur.app.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;

public record UpdateCartItemRequest(
        @Min(value = 1, message = "quantity must be at least 1.")
        @Max(value = 365, message = "quantity cannot exceed 365 days.")
        Integer quantity,

        LocalDate startDate,

        LocalDate endDate
) {
}
