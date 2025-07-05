package re1kur.app.core.car.details;

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
