package re1kur.rentalservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import re1kur.rentalservice.service.CarService;

@Controller
@RequestMapping("cars")
public class CarsController {
    private CarService service;

    @Autowired
    public CarsController(CarService service) {
        this.service = service;
    }

    @GetMapping("list")
    public String listCars(@RequestParam(name = "page", required = false) Integer page, Model model) {
        model.addAttribute("cars", service.findAll());
        return "cars/list.html";
    }
}
