package re1kur.app.model.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MakeInformationDto(
        String country,
        String description,
        LocalDate foundedAt,
        String founder,
        String owner
) {
}
