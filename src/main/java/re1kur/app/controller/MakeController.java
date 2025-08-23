package re1kur.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import re1kur.app.core.dto.MakeFullDto;
import re1kur.app.core.payload.MakeUpdatePayload;
import re1kur.app.service.MakeService;

@Controller
@RequestMapping("/makes/{id}")
@RequiredArgsConstructor
public class MakeController {
    private final MakeService service;

    @GetMapping
    public String getMakeProfile(
            Model model,
            @PathVariable(name = "id") Integer id,
            @AuthenticationPrincipal OidcUser user
            ) {
        MakeFullDto make = service.read(id, user);
        model.addAttribute("make", make);
        return "makes/profile.html";
    }

    @GetMapping("/update")
    public String getEditMake(
            @PathVariable("id") Integer makeId,
            Model model,
            @AuthenticationPrincipal OidcUser user
    ) {
        MakeFullDto make = service.read(makeId, user);
        model.addAttribute("make", make);
        return "makes/update.html";
    }

    @PostMapping("/update")
    public String editMake(
            @Valid @ModelAttribute("update") MakeUpdatePayload payload,
            @PathVariable Integer id,
            @AuthenticationPrincipal OidcUser user
    ) {
        service.update(payload, id, user);
        return "redirect:/makes/" + id;
    }
}
