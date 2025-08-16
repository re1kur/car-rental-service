package re1kur.app.core.dto;

import lombok.Builder;

@Builder
public record CarTypeDto(
        Integer id,
        String name
) {
}
