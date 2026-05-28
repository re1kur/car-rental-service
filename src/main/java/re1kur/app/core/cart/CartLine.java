package re1kur.app.core.cart;

import re1kur.app.core.dto.CarFullDto;

import java.time.LocalDate;

public record CartLine(
        CarFullDto car,
        LocalDate startDate,
        LocalDate endDate,
        long days,
        int subtotal
) {
}
