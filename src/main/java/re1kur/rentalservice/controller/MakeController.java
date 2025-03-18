package re1kur.rentalservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import re1kur.rentalservice.dto.make.MakeReadDto;
import re1kur.rentalservice.service.MakeService;

@Controller
@RequestMapping("makes/{id}")
public class MakeController {
    MakeService service;

    @Autowired
    public MakeController(MakeService service) {
        this.service = service;
    }

    @ModelAttribute("make")
    public MakeReadDto make(@PathVariable int id) {
        return service.read(id);
    }

    @GetMapping
    public String getMake() {
        return "/makes/make.html";
    }

}
