package re1kur.app.core.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record FileDto(
        String id,
        String mediaType,
        String url,
        Instant uploadedAt,
        Instant urlExpiresAt
) {
}
