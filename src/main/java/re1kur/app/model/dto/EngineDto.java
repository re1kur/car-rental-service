package re1kur.app.model.dto;

import lombok.Builder;

@Builder
public record EngineDto(
        Integer id,
        String name
) {
}
