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
import re1kur.rentalservice.service.CarService;
import re1kur.rentalservice.service.MakeService;


@Controller
@RequestMapping("cars/{id}")
public class CarController {
    private final CarService service;
    private final MakeService makeService;


    @Autowired
    public CarController(CarService service, MakeService makeService) {
        this.service = service;
        this.makeService = makeService;
    }

    @ModelAttribute("car")
    public CarReadDto car(@PathVariable int id) {
        return service.readById(id, true, true);
    }

    @GetMapping
    public String getCar() {
        return "/cars/car.html";
    }

    @GetMapping("edit")
    public String editCar(@PathVariable int id, Model model) {
        CarUpdateDto car = service.readUpdateById(id);
        model.addAttribute("makes", makeService.readAll());
        model.addAttribute("update", car);
        model.addAttribute("carDetails", car.getDetails());

        return "/cars/car-edit.html";
    }

    @PostMapping("edit")
    @Transactional
    public String updateCar(
            @PathVariable int id,
            @Validated @ModelAttribute("update") CarUpdateDto car,
            @Validated @ModelAttribute("carDetails") CarDetailsUpdateDto carDetails) {
        car.setDetails(carDetails);
        service.updateCar(car, id);
        return "redirect:/cars/" + id;
    }
}
