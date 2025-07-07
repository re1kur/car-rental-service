package re1kur.app.controller.make;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import re1kur.app.core.dto.MakeDto;
import re1kur.app.core.payload.MakeUpdatePayload;
import re1kur.app.service.MakeService;

@Controller
@RequestMapping("/makes/{id}")
@RequiredArgsConstructor
public class MakeController {
    MakeService service;

    @ModelAttribute("make")
    public MakeDto make(@PathVariable Integer id) {
        return service.get(id);
    }

    @GetMapping
    public String getMakeProfile() {
        return "/makes/profile.html";
    }

    @GetMapping("/update")
    public String getEditMake() {
        return "/makes/update.html";
    }

    @PostMapping("/update")
    public String editMake(
            @Valid @ModelAttribute("update") MakeUpdatePayload payload,
            @PathVariable Integer id,
            @RequestParam("title") MultipartFile title
    ) {
        payload.setImage(title);
        service.update(payload, id);
        return "redirect:/makes/" + id;
    }
}
