package re1kur.app.dto.view;

import lombok.Builder;

@Builder
public record CarDetailsView(
        String color,
        Integer mileage,
        String fuelType,
        String transmission,
        Integer seats,
        String description
) {
}
