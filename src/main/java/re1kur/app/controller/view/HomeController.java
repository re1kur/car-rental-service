package re1kur.app.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import re1kur.app.dto.filter.CarFilter;
import re1kur.app.entity.CarType;
import re1kur.app.service.car.CarService;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {
    private final CarService carService;

    @GetMapping
    public String home(Model model, @AuthenticationPrincipal OidcUser user) {
        model.addAttribute("carTypes", CarType.values());
        model.addAttribute("featured",
                carService.readAll(CarFilter.builder().build(), PageRequest.of(0, 6), user).content());
        return "home/home.html";
    }
}
