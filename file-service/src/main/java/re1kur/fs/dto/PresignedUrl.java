package re1kur.fs.dto;

import java.time.Instant;

public record PresignedUrl(String url, Instant expiration) {
}
