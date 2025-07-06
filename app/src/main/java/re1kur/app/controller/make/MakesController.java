package re1kur.app.controller.make;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import re1kur.app.core.make.MakeReadDto;
import re1kur.app.core.make.MakeWriteDto;
import re1kur.app.entity.car.Make;
import re1kur.app.service.MakeService;

import java.util.List;

@Controller
@RequestMapping("makes")
public class MakesController {
    MakeService service;

    @Autowired
    public MakesController(MakeService service) {
        this.service = service;
    }

    @GetMapping("list")
    public String getList(Model model) {
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
    public String createMake(@Validated MakeWriteDto make,
                             @RequestParam("title") MultipartFile title) {
        make.setImage(title);
        MakeReadDto write = service.write(make);
        return "redirect:/makes/" + write.getId();
    }
}
