package re1kur.rentalservice.dto.car.details;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import re1kur.rentalservice.dto.car.CarUpdateDto;

@Builder
@Getter
@Setter
public class CarDetailsUpdateDto {
    private int id;

    private CarUpdateDto car;

    @NotBlank
    @Size(min = 2, max = 20)
    private String color;

    @PositiveOrZero
    private int mileage;

    @NotBlank
    @Size(min = 2, max = 20)
    private String fuelType;

    @NotBlank
    @Size(min = 2, max = 30)
    private String transmission;
}
