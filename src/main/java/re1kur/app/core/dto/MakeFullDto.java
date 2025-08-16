package re1kur.app.core.dto;

import lombok.*;

import java.util.List;

@Builder
public record MakeFullDto(
         Integer id,
         String name,
         MakeInformationDto information,
         FileDto titleImage,
         List<FileDto> images
) {
}