package re1kur.app.dto.view;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MakeInformationView(
        String country,
        String description,
        LocalDate foundedAt,
        String founder,
        String owner
) {
}
