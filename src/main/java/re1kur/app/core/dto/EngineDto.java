package re1kur.app.core.dto;

import lombok.Builder;

@Builder
public record EngineDto(
        Integer id,
        String name
) {
}
