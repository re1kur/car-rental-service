package re1kur.rentalservice.dto.car;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import re1kur.rentalservice.dto.car.details.CarDetailsWriteDto;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarWriteDto {
    @NotNull
    private Integer makeId;

    @NotBlank
    @Size(min = 2, max = 64)
    private String model;

    @Min(1960)
    @Max(2025)
    private Integer year;


    @NotBlank
    @Size(min = 6, max = 6)
    private String licensePlate;

    private CarDetailsWriteDto details;
}
