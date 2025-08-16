package re1kur.app.core.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CarUpdateDto(
        Integer id,
        MakeShortDto make,
        Integer carTypeId,
        Integer engineId,
        String model,
        Integer year,
        String licensePlate,
        CarInformationDto information,
        FileDto titleImage,
        List<FileDto> images
) {
}
