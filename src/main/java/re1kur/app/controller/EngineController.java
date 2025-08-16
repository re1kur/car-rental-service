package re1kur.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
            @PathVariable(name = "id") Integer id
    ) {
        EngineDto engineDto = engineService.read(id);
        model.addAttribute("engine", engineDto);

        return "/engine/profile.html";
    }

    @GetMapping("/update")
    public String getEngineUpdatePage(
            Model model,
            @PathVariable(name = "id") Integer id) {
        EngineDto engine = engineService.read(id);
        model.addAttribute("engine", engine);

        return "/engine/update.html";
    }

    @PostMapping("/update")
    public String engineUpdate(
            @ModelAttribute @Valid EngineUpdatePayload payload,
            @PathVariable(name = "id") Integer id) {
        engineService.update(payload, id);

        return "redirect:/moderator/menu";
    }

    @DeleteMapping("/delete")
    public String engineDelete(
            @PathVariable(name = "id") Integer id) {

        engineService.delete(id);

        return "redirect:/moderator/menu";
    }
}
