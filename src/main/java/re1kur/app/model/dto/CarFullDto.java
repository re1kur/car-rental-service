package re1kur.app.model.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CarFullDto(
        Integer id,
        String model,
        Boolean available,
        Integer year,
        String licensePlate,
        MakeShortDto make,
        CarTypeDto carType,
        EngineDto engine,
        FileDto titleImage,
        List<FileDto> images,
        CarInformationDto information,
        Integer cost
) {
}
