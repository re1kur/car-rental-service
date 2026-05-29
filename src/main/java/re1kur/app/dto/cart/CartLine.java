package re1kur.app.dto.cart;

import re1kur.app.dto.view.CarFullView;

import java.time.LocalDate;

public record CartLine(
        CarFullView car,
        LocalDate startDate,
        LocalDate endDate,
        long days,
        int subtotal
) {
}
