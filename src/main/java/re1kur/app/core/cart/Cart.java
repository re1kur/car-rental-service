package re1kur.app.core.cart;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@SessionScope
public class Cart {
    private final List<CartItem> items = new ArrayList<>();

    public List<CartItem> getItems() {
        return items;
    }

    public boolean contains(Integer carId) {
        return items.stream().anyMatch(item -> item.getCarId().equals(carId));
    }

    public void add(Integer carId, LocalDate startDate, LocalDate endDate) {
        if (!contains(carId)) {
            items.add(new CartItem(carId, startDate, endDate));
        }
    }

    public void remove(Integer carId) {
        items.removeIf(item -> item.getCarId().equals(carId));
    }

    public void updateDates(Integer carId, LocalDate startDate, LocalDate endDate) {
        items.stream()
                .filter(item -> item.getCarId().equals(carId))
                .findFirst()
                .ifPresent(item -> {
                    item.setStartDate(startDate);
                    item.setEndDate(endDate);
                });
    }

    public void clear() {
        items.clear();
    }

    public int count() {
        return items.size();
    }
}
