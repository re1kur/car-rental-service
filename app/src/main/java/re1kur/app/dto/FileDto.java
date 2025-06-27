package re1kur.app.dto;

import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record FileDto(
        String id,
        String mediaType,
        String url,
        ZonedDateTime uploadedAt,
        ZonedDateTime urlExpiresAt
) {
}
