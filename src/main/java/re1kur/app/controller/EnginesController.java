package re1kur.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import re1kur.app.core.dto.EngineDto;
import re1kur.app.core.payload.EnginePayload;
import re1kur.app.service.EngineService;


@Controller
@RequiredArgsConstructor
@RequestMapping("/engines")
public class EnginesController {
    private final EngineService engineService;

    @GetMapping("/")
    public String listRedirect() {
        return "redirect:/engines/list";
    }

    @GetMapping
    public String enginesRedirect() {
        return "redirect:/engines/list";
    }

    @GetMapping("/create")
    public String getEngineCreatePage(Model model) {
        model.addAttribute("engine", new EnginePayload(null));
        return "/engine/create.html";
    }

    @PostMapping("/create")
    public String createEngine(
            @ModelAttribute EnginePayload payload
    ) {
        engineService.create(payload);
        return "redirect:/moderator/menu";
        //TODO: maybe the better redirect on the new engine profile page
    }

    @GetMapping("/list")
    public String getEnginePage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page <= 0 ? 0: page - 1, size);
        Page<EngineDto> pageDtos = engineService.readPage(pageable);

        model.addAttribute("engines", pageDtos.getContent());
        model.addAttribute("size", pageDtos.getSize());
        model.addAttribute("page", pageDtos.getNumber());
        model.addAttribute("totalPages", pageDtos.getTotalPages());

        return "/engine/list.html";
    }
}
