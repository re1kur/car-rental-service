package re1kur.app.controller.car;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import re1kur.app.core.dto.CarDto;
import re1kur.app.core.car.CarUpdateDto;
import re1kur.app.core.car.details.CarDetailsUpdateDto;
import re1kur.app.core.dto.CarFullDto;
import re1kur.app.service.CarService;
import re1kur.app.service.MakeService;


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

    @GetMapping
    public String getCar(
            @PathVariable(name = "id") Integer id,
            Model model
    ) {

        CarFullDto found = service.readFull(id);
        model.addAttribute("car", found);

        return "/cars/profile.html";
    }

    @GetMapping("edit")
    public String editCar(
            @PathVariable int id,
            Model model) {
        CarUpdateDto car = service.readUpdateById(id);
        model.addAttribute("makes", makeService.readAll());
        model.addAttribute("update", car);
        model.addAttribute("images", car.getImages());
        model.addAttribute("carDetails", car.getDetails());

        return "/cars/car-edit.html";
    }

    @PostMapping("edit")
    @Transactional
    public String updateCar(
            @PathVariable int id,
            @Validated @ModelAttribute("update") CarUpdateDto car,
            @Validated @ModelAttribute("carDetails") CarDetailsUpdateDto carDetails
    ) {
        car.setDetails(carDetails);
        service.updateCar(car, id);
        return "redirect:/cars/" + id;
    }
}
