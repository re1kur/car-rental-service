package re1kur.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import re1kur.app.core.dto.CarTypeDto;
import re1kur.app.core.payload.CarTypePayload;
import re1kur.app.service.CarTypeService;

@Controller
@RequestMapping("/car-types")
@RequiredArgsConstructor
public class CarTypesController {
    private final CarTypeService carTypeService;

    @GetMapping
    public String carTypesRedirect() {
        return "redirect:/car-types/list";
    }

    @GetMapping("/create")
    public String getCarTypeCreatePage(Model model) {
        model.addAttribute("carType", new CarTypePayload(null));
        return "/car-type/create.html";
    }

    @PostMapping("/create")
    public String createCarType(
            @ModelAttribute CarTypePayload payload
    ) {
        carTypeService.create(payload);

        return "redirect:/moderator/menu";

        //TODO: maybe the better idea is redirect to new cartype profile page
    }

    @GetMapping("/list")
    public String getCarTypeList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page <= 0 ? 0 : page - 1, size);

        Page<CarTypeDto> pageDtos = carTypeService.readPage(pageable);
        model.addAttribute("carTypes", pageDtos.getContent());
        model.addAttribute("size", pageDtos.getSize());
        model.addAttribute("page", pageDtos.getNumber());
        model.addAttribute("totalPages", pageDtos.getTotalPages());

        return "/car-type/list.html";
    }
}
