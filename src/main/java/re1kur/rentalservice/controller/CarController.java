package re1kur.rentalservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import re1kur.rentalservice.dto.car.CarReadDto;
import re1kur.rentalservice.dto.car.CarUpdateDto;
import re1kur.rentalservice.dto.car.details.CarDetailsUpdateDto;
import re1kur.rentalservice.dto.make.MakeReadDto;
import re1kur.rentalservice.mapper.CarMapper;
import re1kur.rentalservice.service.CarService;
import re1kur.rentalservice.service.MakeService;

import java.util.List;

@Controller
@RequestMapping("cars/{id}")
public class CarController {
    private final CarService service;
    private final MakeService makeService;

    @Autowired
    public CarController(
            CarService service,
            MakeService makeService,
            CarMapper mapper
            ) {
        this.service = service;
        this.makeService = makeService;
    }


    @GetMapping
    public String getCar(
            @PathVariable int id,
            Model model) {
        CarReadDto byId = service.readById(id, true, true);
        model.addAttribute("car", byId);
        return "/cars/car.html";
    }

    @GetMapping("edit")
    public String editCar(@PathVariable int id, Model model) {
        CarReadDto byId = service.readById(id, true, true);
        List<MakeReadDto> makes = makeService.readAll();
        model.addAttribute("car", byId);
        model.addAttribute("makes", makes);
        return "/cars/car-edit.html";
    }

    @PostMapping("edit")
    @Transactional
    public String editCar(
            Model model,
            @ModelAttribute("car") @Validated CarUpdateDto car,
            @ModelAttribute("details") @Validated CarDetailsUpdateDto details,
            BindingResult bindingResult
            ) {
        System.out.println();
        details.setCar(car);

        if (bindingResult.hasErrors()) {
            model.addAttribute("car", car);
            model.addAttribute("makes", makeService.readAll());
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "/cars/car-edit.html";
        }
        CarReadDto updated  = service.updateCar(car);
        model.addAttribute("car", updated);
        return "/cars/car.html";
    }
}
