package re1kur.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    @GetMapping("/")
    public String getMenuRedirect() {
        return "redirect:/admin/menu";
    }

    @GetMapping("/menu")
    public String getMenuResponsibilities() {
        return "admin/menu.html";
    }
}
