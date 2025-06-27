package re1kur.fs.dto;

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
