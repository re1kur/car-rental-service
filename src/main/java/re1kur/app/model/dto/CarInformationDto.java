package re1kur.app.model.dto;

import lombok.Builder;

@Builder
public record CarInformationDto(
        String color,
        Integer mileage,
        String fuelType,
        String transmission,
        Integer seats,
        String description
) {
}
