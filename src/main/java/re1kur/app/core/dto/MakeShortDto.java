package re1kur.app.core.dto;

import lombok.Builder;

@Builder
public record MakeShortDto(
        Integer id,
        String name
) {
}
