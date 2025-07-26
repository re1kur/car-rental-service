package re1kur.app.controller.make;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
            @PathVariable(name = "id") Integer id
    ) {
        model.addAttribute("make", service.read(id));
        return "/makes/profile.html";
    }

    @GetMapping("/update")
    public String getEditMake(
            @PathVariable("id") Integer makeId,
            Model model
    ) {
        model.addAttribute("make", service.read(makeId));
        return "/makes/update.html";
    }

    @PostMapping("/update")
    public String editMake(
            @Valid @ModelAttribute("update") MakeUpdatePayload payload,
            @PathVariable Integer id
    ) {
        service.update(payload, id);
        return "redirect:/makes/" + id;
    }
}
