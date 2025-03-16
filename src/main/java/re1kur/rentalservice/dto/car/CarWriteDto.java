package re1kur.rentalservice.dto.car;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import re1kur.rentalservice.dto.car.details.CarDetailsWriteDto;
import re1kur.rentalservice.dto.car.images.CarImageWriteDto;

import java.util.List;

@Data
@Builder
public class CarWriteDto {
    @NotNull
    private int makeId;

    @NotBlank
    @NotNull
    @Size(min = 2, max = 64)
    private String model;

    @Positive
    private int year;

    @Size(min = 6, max = 6)
    @NotNull
    @NotBlank
    private String licensePlate;

    private CarDetailsWriteDto details;
    private CarImageWriteDto image;
}
