package re1kur.app.model.dto;

import lombok.Builder;

import java.time.Instant;
import java.time.LocalDateTime;

@Builder
public record FileDto(
        String id,
        String mediaType,
        String url,
        LocalDateTime uploadedAt,
        LocalDateTime urlExpiresAt
) {
}
