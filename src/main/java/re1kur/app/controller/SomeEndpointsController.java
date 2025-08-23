package re1kur.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class SomeEndpointsController {
    @Value("${custom.profile-url}")
    private String profileUrl;

    private ClientRegistrationRepository clientRegistrationRepository;

    @GetMapping("/oauth2/account")
    public String redirectProfile () {
        return "redirect:" + profileUrl;
    }
}
