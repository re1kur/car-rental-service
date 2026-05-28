package re1kur.app.core.dto;

import lombok.Builder;
import re1kur.app.entity.CarType;
import re1kur.app.entity.Engine;

@Builder
public record CarDto(
        Integer id,
        String model,
        Integer year,
        String licensePlate,
        Integer cost,
        MakeShortDto make,
        CarType carType,
        Engine engine,
        FileDto titleImage) {
}
