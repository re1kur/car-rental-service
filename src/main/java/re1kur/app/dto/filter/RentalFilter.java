package re1kur.app.dto.filter;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record RentalFilter (
        Integer carId,
        LocalDate date
) {}
