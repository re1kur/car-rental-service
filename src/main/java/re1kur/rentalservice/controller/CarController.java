package re1kur.rentalservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import re1kur.rentalservice.dto.car.CarReadDto;
import re1kur.rentalservice.service.CarService;

@Controller
@RequestMapping("cars/{id}")
public class CarController {
    private final CarService service;

    @Autowired
    public CarController(CarService service) {
        this.service = service;
    }


    @GetMapping
    public String getCar(
            @PathVariable int id,
            Model model) {
        CarReadDto byId = service.findById(id, true, true);
        model.addAttribute("car", byId);
        return "/cars/car.html";
    }

    @GetMapping("edit")
    public String editCar(@PathVariable int id, Model model) {
        CarReadDto byId = service.findById(id, true, true);
        model.addAttribute("car", byId);
        return "/cars/car-edit.html";
    }
}
