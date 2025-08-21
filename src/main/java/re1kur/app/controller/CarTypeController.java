package re1kur.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import re1kur.app.core.dto.CarTypeDto;
import re1kur.app.core.payload.CarTypeUpdatePayload;
import re1kur.app.service.CarTypeService;

@Controller
@RequestMapping("/car-types/{id}")
@RequiredArgsConstructor
public class CarTypeController {
    private final CarTypeService carTypeService;

    @GetMapping
    public String getCarType(
            Model model,
            @PathVariable(name = "id") Integer id
    ) {
        CarTypeDto carTypeDto = carTypeService.read(id);
        model.addAttribute("carType", carTypeDto);

        return "car-types/profile.html";
    }

    @GetMapping("/update")
    public String getCarTypeUpdatePage(
            @PathVariable(name = "id") Integer id,
            Model model
    ) {
        CarTypeDto type = carTypeService.read(id);
        model.addAttribute("carType", type);
        return "car-types/update.html";
    }

    @PostMapping("/update")
    public String carTypeUpdate(
            @ModelAttribute @Valid CarTypeUpdatePayload payload,
            @PathVariable(name = "id") Integer id
    ) {
        carTypeService.update(payload, id);
        return "redirect:/moderator/menu";
    }

    @DeleteMapping("/delete")
    public String carTypeDelete(
            @PathVariable(name = "id") Integer id
    ) {
        carTypeService.delete(id);

        return "redirect:/moderator/menu";
    }
}
