package re1kur.rentalservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import re1kur.rentalservice.dto.make.MakeReadDto;
import re1kur.rentalservice.dto.make.MakeWriteDto;
import re1kur.rentalservice.entity.Make;
import re1kur.rentalservice.service.MakeService;

import java.util.List;

@Controller
@RequestMapping("makes")
public class MakesController {
    MakeService service;

    @Autowired
    public MakesController(
            MakeService service
    ) {
        this.service = service;
    }

    @GetMapping("list")
    public String getList(
            Model model
    ) {
        List<MakeReadDto> makes = service.readAll();
        model.addAttribute("makes", makes);
        return "/makes/makes-list.html";
    }

    @GetMapping("create")
    public String getCreateMake(Model model) {
        model.addAttribute("make", new Make());
        return "/makes/make-create.html";
    }

    @PostMapping("create")
    public String createMake(
            Model model,
            @Validated MakeWriteDto make,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("make", make);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "/makes/make-create.html";
        }
        model.addAttribute("make", service.write(make));
        return "makes/make.html";
    }
}
