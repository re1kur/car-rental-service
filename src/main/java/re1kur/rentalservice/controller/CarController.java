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
    public CarController(
            CarService service,
            MakeService makeService
            ) {
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
    public String editCar(
            @PathVariable int id,
            Model model
    ) {
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
            @Validated @ModelAttribute("carDetails") CarDetailsUpdateDto carDetails,
            Model model,
            BindingResult bindingResult
            ) {
        car.setDetails(carDetails);
        if (carDetails.getMileage() == null && carDetails.getColor().isEmpty() && carDetails.getTransmission() == null && carDetails.getFuelType() == null ) {
            model.addAttribute("makes", makeService.readAll());
            model.addAttribute("update", car);
            model.addAttribute("carDetails", carDetails);
            model.addAttribute("errors", "All details parameters are empty");
            return "/cars/car-edit.html";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("makes", makeService.readAll());
            model.addAttribute("update", car);
            model.addAttribute("carDetails", carDetails);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "/cars/car-edit.html";
        }
        service.updateCar(car, id);
        return "redirect:/cars/" + id;
    }
}
