package re1kur.app.core.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Builder
public record MakeFullDto(
         Integer id,
         String name,
         MakeInformationDto information,
         ImageDto titleImage,
         List<ImageDto> images
) {
}