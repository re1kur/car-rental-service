package re1kur.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import re1kur.app.core.payload.UserPayload;
import re1kur.app.service.UserService;

@Slf4j
@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping("/{id}")
    public String getUser(
            Model model,
            @PathVariable String id) {
        model.addAttribute("user", service.read(id));
        return "/users/user-info.html";
    }

    @GetMapping("/logout")
    public String logout() {
        return "/users/logout.html";
    }

    @GetMapping("/login")
    public String login() {
        return "/users/login.html";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new UserPayload(null, null, null));
        return "/users/register.html";
    }

    @PostMapping("register")
    public String register(@ModelAttribute @Valid UserPayload payload) {
        service.register(payload);
        return "redirect:/users/login";
    }
}
