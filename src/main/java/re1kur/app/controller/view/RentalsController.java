package re1kur.app.controller.view;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import re1kur.app.dto.view.PageView;
import re1kur.app.dto.view.RentalView;
import re1kur.app.dto.filter.RentalAdminFilter;
import re1kur.app.dto.filter.RentalFilter;
import re1kur.app.dto.payload.RentalPayload;
import re1kur.app.service.rental.RentalService;

import java.util.UUID;

@Controller
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class RentalsController {
    private final RentalService rentalService;

    @GetMapping
    public String getRentalsByPrincipal(
            @AuthenticationPrincipal OidcUser user,
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size,
            @ModelAttribute RentalFilter filter,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);
        UUID userId = UUID.fromString(user.getSubject());

        PageView<RentalView> pageView = rentalService.readAllByUser(pageable, userId, filter);

        model.addAttribute("page", pageView);
        model.addAttribute("filter", filter);
        model.addAttribute("carIds", rentalService.readCarIdsByUser(userId));

        return "rentals/list.html";
    }

    @GetMapping("/users")
    public String getRentals(
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size,
            @ModelAttribute RentalAdminFilter filter,
            Model model,
            @AuthenticationPrincipal OidcUser user
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PageView<RentalView> pageView = rentalService.readAll(pageable, filter, user);

        model.addAttribute("page", pageView);
        model.addAttribute("filter", filter);

        return "rentals/list-admin.html";
    }

    @PostMapping("/create")
    public String createRental(
            @ModelAttribute @Valid RentalPayload payload,
            @AuthenticationPrincipal OidcUser user
    ) {
        UUID id = rentalService.create(payload, user);
        return "redirect:/rentals/" + id;
    }

    @GetMapping("/{id}")
    public String getProfile(
            @PathVariable(name = "id") UUID rentalId,
            @AuthenticationPrincipal OidcUser user,
            Model model
    ) {
        RentalView rental = rentalService.readById(rentalId, user);
        model.addAttribute("rental", rental);

        return "rentals/profile.html";
    }

    @DeleteMapping("/{id}")
    public String deleteRental(
            @PathVariable(name = "id") UUID id,
            @AuthenticationPrincipal OidcUser user
    ) {
        rentalService.deleteById(id, user);

        return "redirect:/rentals";
    }
}
