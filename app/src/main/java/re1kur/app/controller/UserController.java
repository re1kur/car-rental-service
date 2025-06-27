package re1kur.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import re1kur.app.dto.user.UserReadDto;
import re1kur.app.dto.user.UserWriteDto;
import re1kur.app.service.UserService;

@Slf4j
@Controller
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping("{id}")
    public String getUser(
            Model model,
            @PathVariable String id) {
        model.addAttribute("user", service.read(id));
        return "/users/user-info.html";
    }

    @GetMapping("logout")
    public String logout() {
        return "/users/logout.html";
    }

    @PostMapping("logout")
    public String logout(SecurityContext context) {
        context.setAuthentication(null);
        return "redirect:/";
    }

    @GetMapping("login")
    public String login() {
        return "/users/login.html";
    }

    @GetMapping("register")
    public String register() {
        return "/users/register.html";
    }

    @PostMapping("login")
    public String login(@RequestBody UserWriteDto dto) {
        log.info("login: {}", dto.toString());
        return "redirect:/";
    }

    @PostMapping("register")
    public String register(@RequestBody UserWriteDto dto) {
        log.info("register: {}", dto.toString());
        return "redirect:/";
    }

//    @PostMapping("login")
//    public String login(@RequestBody @Valid UserWriteDto users) {
//        service.login(user);
//        return "redirect:/";
//    }
//
//    @GetMapping("register")
//    public String register() {
//        return "/users/login.html";
//    }
//
//    @PostMapping("register")
//    public String register(@RequestBody @Valid UserWriteDto user) {
//        service.register(user);
//        return "redirect:/";
//    }
}
