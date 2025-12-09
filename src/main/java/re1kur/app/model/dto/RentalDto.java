package re1kur.app.model.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record RentalDto(
        UUID id,
        UUID userId,
        Integer carId,
        LocalDate startDate,
        LocalDate endDate,
        Integer totalCost
) {
}
