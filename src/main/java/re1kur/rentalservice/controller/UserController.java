package re1kur.rentalservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import re1kur.rentalservice.dto.user.UserWriteDto;
import re1kur.rentalservice.service.UserService;

@Slf4j
@Controller
@RequestMapping("users")
public class UserController {
    UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("register")
    public String getRegister(Model model) {
        model.addAttribute("user", new UserWriteDto());
        return "/users/user-register.html";
    }

    @Transactional
    @PostMapping("register")
    public String register(@Validated @ModelAttribute("user") UserWriteDto user) {
        int id = service.write(user);
        return "redirect:/users/login?registered";
    }

    @GetMapping("{id}")
    public String getUser(
            Model model,
            @PathVariable int id) {
        model.addAttribute("user", service.read(id));
        return "/users/user-info.html";
    }

    @GetMapping("login")
    public String getLogin() {
        return "/users/login.html";
    }

    @GetMapping("logout")
    public String logout() {
        return "/users/logout.html";
    }

    @PostMapping("logout")
    public String logout(Authentication authentication) {
        authentication.setAuthenticated(false);
        return "redirect:/users/login?logout";
    }
}
