package re1kur.rentalservice.dto.car.details;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CarDetailsReadDto {
    int id;
    String color;
    Integer mileage;
    String fuelType;
    String transmission;
}
