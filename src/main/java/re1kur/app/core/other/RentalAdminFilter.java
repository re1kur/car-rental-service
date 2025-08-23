package re1kur.app.core.other;

import java.time.LocalDate;
import java.util.UUID;

public record RentalAdminFilter(
        UUID userId,
        Integer carId,
        LocalDate date
) {
}
