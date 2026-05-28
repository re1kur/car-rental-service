package re1kur.app.core.dto;

import lombok.Builder;
import re1kur.app.entity.CarType;
import re1kur.app.entity.Engine;

import java.util.List;

@Builder
public record CarFullDto(
        Integer id,
        String model,
        Boolean available,
        Integer year,
        String licensePlate,
        MakeShortDto make,
        CarType carType,
        Engine engine,
        FileDto titleImage,
        List<FileDto> images,
        CarInformationDto information,
        Integer cost
) {
}
