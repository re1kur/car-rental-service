package re1kur.rentalservice.dto.car.details;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDetailsWriteDto {

    @Size(max = 20)
    private String color;

    @PositiveOrZero
    private Integer mileage;

    @Size(max = 20)
    private String fuelType;

    @Size(max = 30)
    private String transmission;
}
