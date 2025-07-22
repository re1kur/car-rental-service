package re1kur.app.controller.make;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import re1kur.app.core.dto.MakeDto;
import re1kur.app.core.payload.MakePayload;
import re1kur.app.service.MakeService;

import java.util.List;

@Controller
@RequestMapping("/makes")
@RequiredArgsConstructor
public class MakesController {
    private final MakeService service;

    @GetMapping
    public String redirectList() {
        return "redirect:/makes/list";
    }

    @GetMapping("/list")
    public String getList(Model model) {
        List<MakeDto> makes = service.readAll();
        model.addAttribute("makes", makes);
        return "/makes/list.html";
    }

    @GetMapping("/create")
    public String getCreateMake(Model model) {
        model.addAttribute("make", null);
        return "/makes/create.html";
    }

    @PostMapping("/create")
    public String createMake(
            @ModelAttribute @Valid MakePayload payload,
            @RequestParam("titleImg") MultipartFile titleImg
    ) {
        service.create(payload, titleImg);
        return "redirect:/moderator/menu";
    }
}
