package re1kur.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import re1kur.app.core.dto.CarTypeDto;
import re1kur.app.core.dto.PageDto;
import re1kur.app.core.payload.CarTypePayload;
import re1kur.app.service.CarTypeService;

@Controller
@RequestMapping("/car-types")
@RequiredArgsConstructor
public class CarTypesController {
    private final CarTypeService carTypeService;

    @GetMapping
    public String getCarTypes(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            Model model,
            @AuthenticationPrincipal OidcUser user
            ) {
        Pageable pageable = PageRequest.of(page <= 0 ? 0 : page - 1, size);

        PageDto<CarTypeDto> pageDto = carTypeService.readAllAsPage(pageable, user);
        model.addAttribute("page", pageDto);

        return "car-types/list.html";
    }

    @GetMapping("/create")
    public String getCarTypeCreatePage(Model model) {
        model.addAttribute("carType", new CarTypePayload(null));
        return "car-types/create.html";
    }

    @PostMapping("/create")
    public String createCarType(
            @ModelAttribute CarTypePayload payload,
            @AuthenticationPrincipal OidcUser user
    ) {
        Integer id = carTypeService.create(payload, user);

        return "redirect:/car-types/" + id;
    }
}
