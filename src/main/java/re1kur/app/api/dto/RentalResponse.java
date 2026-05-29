package re1kur.app.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RentalResponse(
        UUID id,
        Integer carId,
        String make,
        String model,
        String imageUrl,
        LocalDate startDate,
        LocalDate endDate,
        Integer totalCost
) {
}
