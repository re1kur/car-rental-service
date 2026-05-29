package re1kur.app.dto.view;

import lombok.Builder;

@Builder
public record MakeShortView(
        Integer id,
        String name
) {
}
