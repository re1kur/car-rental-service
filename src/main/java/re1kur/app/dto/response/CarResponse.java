package re1kur.app.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CarResponse(
        Integer id,
        Integer makeId,
        String make,
        String model,
        Integer year,
        String licensePlate,
        Integer pricePerDay,
        Boolean available,
        CarTypeResponse carType,
        EngineResponse engine,
        String imageUrl,
        // detail-only (null in list view)
        String description,
        String color,
        Integer seats,
        Integer mileage,
        String fuelType,
        String transmission,
        List<String> images
) {
}
