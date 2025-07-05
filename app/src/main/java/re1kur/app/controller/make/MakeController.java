package re1kur.app.controller.make;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import re1kur.app.core.make.MakeReadDto;
import re1kur.app.core.make.MakeUpdateDto;
import re1kur.app.service.MakeService;

@Controller
@RequestMapping("makes/{id}")
public class MakeController {
    MakeService service;

    @Autowired
    public MakeController(MakeService service) {
        this.service = service;
    }

    @ModelAttribute("make")
    public MakeReadDto make(@PathVariable int id) {
        return service.read(id);
    }

    @GetMapping
    public String getMake() {
        return "/makes/make.html";
    }

    @GetMapping("edit")
    public String getEditMake(@PathVariable int id, Model model) {
        model.addAttribute("update", service.readUpdateById(id));
        return "/makes/make-edit.html";
    }

    @PostMapping("edit")
    public String editMake(
            @Validated @ModelAttribute("update") MakeUpdateDto update,
            @PathVariable int id,
            @RequestParam("title") MultipartFile title) {
        update.setImage(title);
        service.updateMake(update, id);
        return "redirect:/makes/" + id;
    }

}
