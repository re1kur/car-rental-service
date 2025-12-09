package re1kur.app.model.dto;

import lombok.Builder;

@Builder
public record MakeDto(
        Integer id,
        String name,
        FileDto titleImage
) {
}
