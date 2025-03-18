package re1kur.rentalservice.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import re1kur.rentalservice.dto.user.UserWriteDto;
import re1kur.rentalservice.service.UserService;

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
        return "redirect:/users/" + id;
    }

    @GetMapping("{id}")
    public String getUser(Model model, @PathVariable int id) {
        model.addAttribute("user", service.read(id));
        return "/users/user-info.html";
    }
}
