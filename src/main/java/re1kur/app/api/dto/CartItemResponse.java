package re1kur.app.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CartItemResponse(
        Integer carId,
        String make,
        String model,
        Integer pricePerDay,
        long quantity,
        LocalDate startDate,
        LocalDate endDate,
        Integer subtotal,
        Boolean available,
        String imageUrl
) {
}
