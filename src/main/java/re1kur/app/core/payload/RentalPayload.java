package re1kur.app.core.payload;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RentalPayload(
        @NotNull
        Integer carId,
        @FutureOrPresent(message = "Start date cannot be in the past.")
        @NotNull
        LocalDate startDate,
        @FutureOrPresent(message = "End date cannot be in the past.")
        @NotNull
        LocalDate endDate
) {
}
