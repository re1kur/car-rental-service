package re1kur.app.dto.view;

import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record RentalView(
        UUID id,
        UUID userId,
        Integer carId,
        String carMake,
        String carModel,
        String carImageUrl,
        LocalDate startDate,
        LocalDate endDate,
        Integer totalCost
) {
}
