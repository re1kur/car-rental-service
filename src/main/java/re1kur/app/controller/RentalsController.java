package re1kur.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import re1kur.app.core.dto.PageDto;
import re1kur.app.core.dto.RentalDto;
import re1kur.app.core.other.RentalAdminFilter;
import re1kur.app.core.other.RentalFilter;
import re1kur.app.core.payload.RentalPayload;
import re1kur.app.service.RentalService;

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

        PageDto<RentalDto> pageDto = rentalService.readAllByUser(pageable, userId, filter);

        model.addAttribute("page", pageDto);
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
        PageDto<RentalDto> pageDto = rentalService.readAll(pageable, filter, user);

        model.addAttribute("page", pageDto);
        model.addAttribute("filter", filter);

        return "rentals/list-admin.html";
    }

    @PostMapping("/create")
    public String createRental(
            @ModelAttribute @Valid RentalPayload payload,
            @AuthenticationPrincipal OidcUser user
            ) {
        UUID id = rentalService.create(payload, UUID.fromString(user.getUserInfo().getSubject()));
        return "redirect:/rentals/" + id;
    }

    @GetMapping("/{id}")
    public String getProfile(
            @PathVariable(name = "id") UUID rentalId,
            Model model,
            @AuthenticationPrincipal OidcUser user
    ) {
        RentalDto rental = rentalService.readById(rentalId, user);
        model.addAttribute("rental", rental);

        return "rentals/profile.html";
    }
}
