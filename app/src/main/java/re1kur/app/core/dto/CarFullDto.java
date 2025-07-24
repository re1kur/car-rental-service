package re1kur.app.core.dto;

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
        ImageDto titleImage,
        List<ImageDto> images,
        CarInformationDto information
) {
}
