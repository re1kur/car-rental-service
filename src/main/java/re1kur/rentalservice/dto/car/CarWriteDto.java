package re1kur.rentalservice.dto.car;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import re1kur.rentalservice.dto.car.details.CarDetailsWriteDto;
import re1kur.rentalservice.dto.car.images.CarImageWriteDto;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarWriteDto {
    @NotNull(message = "The make should be selected.")
    private Integer makeId;

    @NotBlank(message = "The model mustn't only name with backspace chars.")
    @Size(message = "The model name must be greater and " +
                    "lesser than 2 and 64 chars respectively.",
            min = 2, max = 64)
    private String model;

    @Min(value = 1960, message = "Our service only rents cars after 1960.")
    @Max(value = 2025, message = "The year of release cannot be after current year.")
    private Integer year;


    @NotBlank(message = "The license plate mustn't name with backspace chars.")
    @Size(message = "Length of license plate must be 6 chars.", min = 6, max = 6)
    private String licensePlate;

    private CarDetailsWriteDto details;

    private CarImageWriteDto titleImage;

    private CarImageWriteDto image;
}
