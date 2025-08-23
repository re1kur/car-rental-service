package re1kur.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import re1kur.app.core.dto.EngineDto;
import re1kur.app.core.payload.EngineUpdatePayload;
import re1kur.app.service.EngineService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/engines/{id}")
public class EngineController {
    private final EngineService engineService;

    @GetMapping
    public String getCarType(
            Model model,
            @PathVariable(name = "id") Integer id,
            @AuthenticationPrincipal OidcUser user
    ) {
        EngineDto engineDto = engineService.read(id, user);
        model.addAttribute("engine", engineDto);

        return "engines/profile.html";
    }

    @GetMapping("/update")
    public String getEngineUpdatePage(
            Model model,
            @PathVariable(name = "id") Integer id,
            @AuthenticationPrincipal OidcUser user
    ) {
        EngineDto engine = engineService.read(id, user);
        model.addAttribute("engine", engine);

        return "engines/update.html";
    }

    @PostMapping("/update")
    public String engineUpdate(
            @ModelAttribute @Valid EngineUpdatePayload payload,
            @PathVariable(name = "id") Integer id,
            @AuthenticationPrincipal OidcUser user
    ) {
        engineService.update(payload, id, user);

        return "redirect:/engines/" + id;
    }

    @DeleteMapping("/delete")
    public String engineDelete(
            @PathVariable(name = "id") Integer id,
            @AuthenticationPrincipal OidcUser user
    ) {
        engineService.delete(id, user);

        return "redirect:/engines";
    }
}
