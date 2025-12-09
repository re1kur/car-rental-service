package re1kur.app.model.payload;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record ImagePayload(
        String id,
        String url,
        OffsetDateTime uploadedAt,
        OffsetDateTime urlExpiresAt
) {
}
