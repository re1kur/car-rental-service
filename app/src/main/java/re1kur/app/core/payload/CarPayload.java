package re1kur.app.core.payload;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import re1kur.app.core.car.details.CarDetailsWriteDto;
import re1kur.app.core.car.images.CarImageWriteDto;

@Builder
public record CarPayload (
    @NotNull(message = "The make must be defined.")
    @Positive(message = "The make ID cannot be negative or zero.")
    Integer makeId,

    @NotNull(message = "The car type must be defined.")
    @Positive(message = "The car type ID cannot be negative or zero.")
    Integer carTypeId,

    @NotNull(message = "The engine must be defined.")
    @Positive(message = "The engine ID cannot be negative or zero.")
    Integer engineId,

    @NotBlank(message = "The model cannot be empty or contain backspace characters.")
    @Size(min = 2, max = 64, message = "The model must be between 2 and 64 characters long.")
    String model,

    @Min(value = 1960, message = "Our service only rents cars after 1960.")
    @Max(value = 2025, message = "The year of release cannot be after current year.")
    Integer year,


    @NotBlank(message = "The license plate must be defined.")
    @Size(message = "The license plate must be 6 characters long.", min = 6, max = 6)
    String licensePlate,

    String description,

    String color,

    Integer seats,

    Integer mileage,

    String fuelType,

    String transmission
) {}
