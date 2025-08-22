package re1kur.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import re1kur.app.core.dto.MakeDto;
import re1kur.app.core.dto.PageDto;
import re1kur.app.core.payload.MakePayload;
import re1kur.app.service.MakeService;

@Controller
@RequestMapping("/makes")
@RequiredArgsConstructor
public class MakesController {
    private final MakeService service;

    @GetMapping("/")
    public String redirectListSlash() {
        return "redirect:/makes";
    }

    @GetMapping
    public String getMakes(
            Model model,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "page", required = false, defaultValue = "5") Integer size,
            @RequestParam(name = "name", required = false) String name
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PageDto<MakeDto> makes = service.readAll(name, pageable);
        model.addAttribute("page", makes);
        model.addAttribute("name", name);

        return "makes/list.html";
    }

    @GetMapping("/create")
    public String getCreateMake(
            Model model
    ) {
        model.addAttribute("make", null);
        return "makes/create.html";
    }

    @PostMapping("/create")
    public String createMake(
            @ModelAttribute @Valid MakePayload payload,
            @RequestParam(value = "title", required = false) MultipartFile title,
            @RequestParam(value = "file", required = false) MultipartFile[] files
    ) {
        service.create(payload, title, files);
        return "redirect:/moderator/menu";
    }
}