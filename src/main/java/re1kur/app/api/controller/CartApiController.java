package re1kur.app.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import re1kur.app.api.dto.AddCartItemRequest;
import re1kur.app.api.dto.CartItemResponse;
import re1kur.app.api.dto.CartResponse;
import re1kur.app.api.dto.UpdateCartItemRequest;
import re1kur.app.core.cart.Cart;
import re1kur.app.core.cart.CartItem;
import re1kur.app.core.dto.CarFullDto;
import re1kur.app.core.exception.CarNotFoundException;
import re1kur.app.service.CarService;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "Cart", description = "Session cart (quantity = rental days). Requires authentication.")
@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartApiController {

    private final Cart cart;
    private final CarService carService;

    @Operation(summary = "Get current user's cart")
    @GetMapping
    public CartResponse get(@AuthenticationPrincipal OidcUser user) {
        return build(user);
    }

    @Operation(summary = "Add a car to the cart (quantity = rental days)")
    @PostMapping("/items")
    public ResponseEntity<CartResponse> add(
            @Valid @RequestBody AddCartItemRequest request,
            @AuthenticationPrincipal OidcUser user
    ) {
        CarFullDto car = carService.readFull(request.carId(), user);
        if (Boolean.FALSE.equals(car.available())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Car [%d] is not available for rent.".formatted(request.carId()));
        }
        int days = request.quantity() != null ? request.quantity() : 1;
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(days - 1L);

        if (cart.contains(request.carId())) {
            cart.updateDates(request.carId(), start, end);
        } else {
            cart.add(request.carId(), start, end);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(build(user));
    }

    @Operation(summary = "Update quantity (rental days) of a cart item")
    @PutMapping("/items/{carId}")
    public CartResponse update(
            @PathVariable Integer carId,
            @Valid @RequestBody UpdateCartItemRequest request,
            @AuthenticationPrincipal OidcUser user
    ) {
        CartItem item = require(carId);
        LocalDate start = item.getStartDate();
        cart.updateDates(carId, start, start.plusDays(request.quantity() - 1L));
        return build(user);
    }

    @Operation(summary = "Remove a car from the cart")
    @DeleteMapping("/items/{carId}")
    public CartResponse remove(@PathVariable Integer carId, @AuthenticationPrincipal OidcUser user) {
        require(carId);
        cart.remove(carId);
        return build(user);
    }

    @Operation(summary = "Clear the whole cart")
    @DeleteMapping("/clear")
    public CartResponse clear(@AuthenticationPrincipal OidcUser user) {
        cart.clear();
        return build(user);
    }

    private CartItem require(Integer carId) {
        return cart.getItems().stream()
                .filter(i -> i.getCarId().equals(carId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Car [%d] is not in the cart.".formatted(carId)));
    }

    private CartResponse build(OidcUser user) {
        List<CartItemResponse> lines = new ArrayList<>();
        int total = 0;
        for (CartItem item : new ArrayList<>(cart.getItems())) {
            CarFullDto car;
            try {
                car = carService.readFull(item.getCarId(), user);
            } catch (CarNotFoundException e) {
                cart.remove(item.getCarId());
                continue;
            }
            long days = Period.between(item.getStartDate(), item.getEndDate()).getDays() + 1L;
            int subtotal = car.cost() != null ? (int) (car.cost() * days) : 0;
            total += subtotal;
            lines.add(new CartItemResponse(
                    car.id(),
                    car.make() != null ? car.make().name() : null,
                    car.model(),
                    car.cost(),
                    days,
                    item.getStartDate(),
                    item.getEndDate(),
                    subtotal,
                    car.available(),
                    car.titleImage() != null ? car.titleImage().url() : null
            ));
        }
        return new CartResponse(lines, lines.size(), total);
    }
}
