package re1kur.app.dto.view;

import lombok.Builder;

@Builder
public record MakeView(
        Integer id,
        String name,
        FileView titleImage
) {
}
