package re1kur.app.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/moderator")
@RequiredArgsConstructor
public class ModeratorController {

    @GetMapping("/")
    public String getMenuRedirect() {
        log.info("redirect to menu");
        return "redirect:/moderator/menu";
    }

    @GetMapping("menu")
    public String getMenuResponsibilities() {
        log.info("moderator menu");
        return "/moderator/menu.html";
    }

}
