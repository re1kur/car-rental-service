package re1kur.app.controller.cartype;

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
            @PathVariable Integer id
    ) {
        CarTypeDto carTypeDto = carTypeService.get(id);
        model.addAttribute("carType", carTypeDto);

        return "/car-type/profile.html";
    }

    @GetMapping("/update")
    public String getCarTypeUpdatePage(
            @PathVariable Integer id,
            Model model
    ) {
        CarTypeDto type = carTypeService.get(id);
        model.addAttribute("carType", type);
        return "/car-type/update.html";
    }

    @PostMapping("/update")
    public String carTypeUpdate(
            @ModelAttribute @Valid CarTypeUpdatePayload payload,
            @PathVariable Integer id
    ) {
        carTypeService.update(payload, id);
        return "redirect:/moderator/menu";
    }

    @DeleteMapping("/delete")
    public String carTypeDelete(
            @PathVariable Integer id
    ) {
        carTypeService.delete(id);

        return "redirect:/moderator/menu";
    }
}
