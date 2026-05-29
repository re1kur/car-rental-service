package re1kur.app.controller.view;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import re1kur.app.dto.cart.Cart;
import re1kur.app.dto.cart.CartItem;
import re1kur.app.dto.cart.CartLine;
import re1kur.app.dto.view.CarFullView;
import re1kur.app.exception.CarNotFoundException;
import re1kur.app.dto.payload.RentalPayload;
import re1kur.app.service.car.CarService;
import re1kur.app.service.rental.RentalService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final Cart cart;
    private final CarService carService;
    private final RentalService rentalService;

    @GetMapping
    public String view(Model model, @AuthenticationPrincipal OidcUser user) {
        List<CartLine> lines = new ArrayList<>();
        int total = 0;

        for (CartItem item : new ArrayList<>(cart.getItems())) {
            CarFullView car;
            try {
                car = carService.readFull(item.getCarId(), user);
            } catch (CarNotFoundException e) {
                cart.remove(item.getCarId());
                continue;
            }
            long days = ChronoUnit.DAYS.between(item.getStartDate(), item.getEndDate()) + 1;
            int subtotal = (car.cost() != null ? car.cost() : 0) * (int) days;
            total += subtotal;
            lines.add(new CartLine(car, item.getStartDate(), item.getEndDate(), days, subtotal));
        }

        model.addAttribute("lines", lines);
        model.addAttribute("total", total);
        return "cart/cart.html";
    }

    @PostMapping("/add/{id}")
    public String add(@PathVariable Integer id, @AuthenticationPrincipal OidcUser user, HttpServletRequest request) {
        CarFullView car = carService.readFull(id, user);
        if (Boolean.TRUE.equals(car.available())) {
            LocalDate today = LocalDate.now();
            cart.add(id, today, today.plusDays(1));
        }
        String referer = request.getHeader("referer");
        return "redirect:" + (referer != null ? referer : "/cars");
    }

    @PostMapping("/remove/{id}")
    public String remove(@PathVariable Integer id) {
        cart.remove(id);
        return "redirect:/cart";
    }

    @PostMapping("/items/{id}")
    public String updateDates(
            @PathVariable Integer id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            RedirectAttributes redirect
    ) {
        if (startDate.isBefore(LocalDate.now()) || endDate.isBefore(startDate)) {
            redirect.addFlashAttribute("error",
                    "Invalid dates: start cannot be in the past, and end cannot be before start.");
        } else {
            cart.updateDates(id, startDate, endDate);
        }
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout(@AuthenticationPrincipal OidcUser user) {
        if (cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }
        List<RentalPayload> payloads = cart.getItems().stream()
                .map(item -> new RentalPayload(item.getCarId(), item.getStartDate(), item.getEndDate()))
                .toList();

        rentalService.createAll(payloads, user);
        cart.clear();
        return "redirect:/rentals";
    }
}
