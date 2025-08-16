package re1kur.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import re1kur.app.core.dto.CarUpdateDto;
import re1kur.app.core.payload.CarUpdatePayload;
import re1kur.app.core.dto.CarFullDto;
import re1kur.app.service.CarService;
import re1kur.app.service.CarTypeService;
import re1kur.app.service.EngineService;
import re1kur.app.service.MakeService;


@Controller
@RequestMapping("cars/{id}")
@RequiredArgsConstructor
public class CarController {
    private final CarService service;
    private final MakeService makeService;
    private final EngineService engineService;
    private final CarTypeService carTypeService;

    @GetMapping
    public String getCar(
            @PathVariable(name = "id") Integer id,
            Model model
    ) {

        CarFullDto found = service.readFull(id);
        model.addAttribute("car", found);

        return "/cars/profile.html";
    }

    @GetMapping("/update")
    public String getUpdatePage(
            @PathVariable int id,
            Model model
    ) {
        CarUpdateDto car = service.readUpdateById(id);
        model.addAttribute("makes", makeService.readAll());
        model.addAttribute("engines", engineService.readAll());
        model.addAttribute("carTypes", carTypeService.readAll());
        model.addAttribute("car", car);

        return "/cars/update.html";
    }

    @PostMapping("/update")
    public String updateCar(
            @PathVariable Integer id,
            @Valid @ModelAttribute("car") CarUpdatePayload payload
    ) {
        service.updateCar(payload, id);
        return "redirect:/cars/" + id;
    }
}
