package re1kur.app.controller.view;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import re1kur.app.dto.view.CarUpdateView;
import re1kur.app.dto.payload.CarUpdatePayload;
import re1kur.app.dto.view.CarFullView;
import re1kur.app.entity.CarType;
import re1kur.app.entity.Engine;
import re1kur.app.service.car.CarService;
import re1kur.app.service.make.MakeService;


@Controller
@RequestMapping("/cars/{id}")
@RequiredArgsConstructor
public class CarController {
    private final CarService service;
    private final MakeService makeService;

    @GetMapping
    public String getCar(
            @PathVariable(name = "id") Integer id,
            @AuthenticationPrincipal OidcUser user,
            Model model
    ) {
        CarFullView found = service.readFull(id, user);
        model.addAttribute("car", found);

        return "cars/profile.html";
    }

    @GetMapping("/update")
    public String getUpdatePage(
            @PathVariable int id,
            Model model
    ) {
        CarUpdateView car = service.readUpdateById(id);
        model.addAttribute("makes", makeService.readAll());
        model.addAttribute("carTypes", CarType.values());
        model.addAttribute("engines", Engine.values());
        model.addAttribute("car", car);

        return "cars/update.html";
    }

    @PostMapping("/update")
    public String updateCar(
            @PathVariable Integer id,
            @ModelAttribute("car") @Valid CarUpdatePayload payload,
            @RequestParam(value = "title", required = false) MultipartFile title,
            @RequestParam(value = "file", required = false) MultipartFile[] files,
            @AuthenticationPrincipal OidcUser user
    ) {
        service.updateCar(payload, title, files, id, user);
        return "redirect:/cars/" + id;
    }

    @DeleteMapping
    public String deleteCar(
            @PathVariable(name = "id") Integer id,
            @AuthenticationPrincipal OidcUser user
    ) {
        service.delete(id, user);

        return "redirect:/cars";
    }
}
