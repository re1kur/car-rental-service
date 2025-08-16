package re1kur.app.core.other;

import java.time.Instant;

public record PresignedUrl(String url, Instant expiration) {
}
