package re1kur.app.dto.request;

import jakarta.validation.constraints.NotNull;

public record AvailabilityRequest(
        @NotNull(message = "The availability flag must be provided.")
        Boolean available
) {
}
