package re1kur.app.core.other;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record RentalFilter (
        Integer carId,
        LocalDate date
) {}
