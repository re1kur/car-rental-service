package re1kur.app.model.dto;

import lombok.Builder;

@Builder
public record CarDto(
        Integer id,
        String model,
        Integer year,
        String licensePlate,
        MakeShortDto make,
        CarTypeDto carType,
        EngineDto engine,
        FileDto titleImage) {
}
