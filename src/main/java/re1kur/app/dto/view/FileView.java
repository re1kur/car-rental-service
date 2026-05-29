package re1kur.app.dto.view;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FileView(
        String id,
        String mediaType,
        String url,
        LocalDateTime uploadedAt
) {
}
