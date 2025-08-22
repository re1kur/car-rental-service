package re1kur.app.core.dto;

import lombok.Builder;

@Builder
public record MakeDto(
        Integer id,
        String name,
        FileDto titleImage
) {
}
