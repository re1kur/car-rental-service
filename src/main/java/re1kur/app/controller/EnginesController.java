package re1kur.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import re1kur.app.core.dto.EngineDto;
import re1kur.app.core.dto.PageDto;
import re1kur.app.core.payload.EnginePayload;
import re1kur.app.service.EngineService;


@Controller
@RequiredArgsConstructor
@RequestMapping("/engines")
public class EnginesController {
    private final EngineService engineService;

    @GetMapping("/")
    public String listRedirect() {
        return "redirect:/engines";
    }

    @GetMapping
    public String getEngines(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page <= 0 ? 0: page - 1, size);
        PageDto<EngineDto> pageDto = engineService.readPage(pageable);

        model.addAttribute("page", pageDto);
        return "engines/list.html";
    }

    @GetMapping("/create")
    public String getEngineCreatePage(Model model) {
        model.addAttribute("engine", new EnginePayload(null));
        return "engines/create.html";
    }

    @PostMapping("/create")
    public String createEngine(
            @ModelAttribute EnginePayload payload
    ) {
        Integer id = engineService.create(payload);
        return "redirect:/engines/" + id;
    }
}
