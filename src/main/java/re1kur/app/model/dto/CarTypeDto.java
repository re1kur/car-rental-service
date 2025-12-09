package re1kur.app.model.dto;

import lombok.Builder;

@Builder
public record CarTypeDto(
        Integer id,
        String name
) {
}
