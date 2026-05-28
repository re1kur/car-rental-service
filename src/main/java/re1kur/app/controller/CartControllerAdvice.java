package re1kur.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import re1kur.app.core.cart.Cart;

@ControllerAdvice
@RequiredArgsConstructor
public class CartControllerAdvice {
    private final Cart cart;

    @ModelAttribute("cartCount")
    public int cartCount() {
        return cart.count();
    }
}
