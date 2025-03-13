package re1kur.rentalservice.dto.car.details;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CarDetailsReadDto {
    int id;
    String color;
    int mileage;
    String fuelType;
    String transmission;
}
