package re1kur.app.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CartItem {
    private Integer carId;
    private LocalDate startDate;
    private LocalDate endDate;
}
