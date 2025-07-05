package re1kur.app.core.car.details;

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
    @Size(max = 20, message = "The color have to be name lesser than 20 chars.")
    private String color;

    @PositiveOrZero(message = "The mileage cannot be negative.")
    private Integer mileage;

    @Size(max = 20, message = "The fuel type have to be name lesser than 20 chars.")
    private String fuelType;

    @Size(max = 30, message = "The transmission have to be name lesser than 30 chars.")
    private String transmission;
}
