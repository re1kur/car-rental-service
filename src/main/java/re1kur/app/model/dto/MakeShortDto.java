package re1kur.app.model.dto;

import lombok.Builder;

@Builder
public record MakeShortDto(
        Integer id,
        String name
) {
}
