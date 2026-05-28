package re1kur.app.core.dto;

import lombok.Builder;
import re1kur.app.entity.CarType;
import re1kur.app.entity.Engine;

import java.util.List;

@Builder
public record CarUpdateDto(
        Integer id,
        MakeShortDto make,
        CarType carType,
        Engine engine,
        String model,
        Integer year,
        String licensePlate,
        CarInformationDto information,
        FileDto titleImage,
        List<FileDto> images,
        Boolean available,
        Integer cost
) {
}
