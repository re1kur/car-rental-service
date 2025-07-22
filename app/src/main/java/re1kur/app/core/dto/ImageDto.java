package re1kur.app.core.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ImageDto(
        String id,
        String url,
        LocalDateTime uploadedAt
) {
}
