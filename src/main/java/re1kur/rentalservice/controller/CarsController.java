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
import re1kur.rentalservice.dto.car.CarWriteDto;
import re1kur.rentalservice.dto.car.details.CarDetailsUpdateDto;
import re1kur.rentalservice.dto.car.details.CarDetailsWriteDto;
import re1kur.rentalservice.service.CarService;
import re1kur.rentalservice.service.MakeService;

@Controller
@RequestMapping("cars")
public class CarsController {
    private final CarService service;
    private final MakeService makeService;

    @Autowired
    public CarsController(CarService service, MakeService makeService) {
        this.service = service;
        this.makeService = makeService;
    }

    @GetMapping("list")
    public String listCars(Model model) {
        model.addAttribute("cars",
                service.readAll());
        return "cars/cars-list.html";
    }

    @GetMapping("/create")
    public String getCreateCar(Model model) {
        model.addAttribute("makes", makeService.readAll());
        model.addAttribute("write", new CarWriteDto());
        model.addAttribute("carDetails", new CarDetailsWriteDto());
        return "cars/car-create.html";
    }

    @Transactional
    @PostMapping("/create")
    public String createCar(
            @Validated @ModelAttribute("write") CarWriteDto car,
            @Validated @ModelAttribute("carDetails") CarDetailsWriteDto carDetails) {
        car.setDetails(carDetails);
        Integer id = service.writeCar(car);
        return "redirect:/cars/" + id;
    }


    @GetMapping("/make/{id}")
    public String getCarsByMake(Model model, @PathVariable int id) {
        model.addAttribute("cars",
                service.readAllByMake(id));
        return "/cars/cars-list.html";
    }
}
