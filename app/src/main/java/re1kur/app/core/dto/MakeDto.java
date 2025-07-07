package re1kur.app.core.dto;

import lombok.*;

@Builder
public record MakeDto(
        Integer id,
        String name,
        String country,
        String description,
        String titleImageUrl
) {
}
