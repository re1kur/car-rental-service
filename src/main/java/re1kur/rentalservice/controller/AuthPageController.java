package re1kur.rentalservice.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import re1kur.rentalservice.core.dto.user.RegistrationRequest;
import re1kur.rentalservice.core.dto.user.LoginRequest;
import re1kur.rentalservice.service.AuthService;
import re1kur.rentalservice.util.CryptoUtil;
import re1kur.rentalservice.util.RsaKeyUtil;

import java.security.PublicKey;
import java.util.Base64;

@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthPageController {
    private final AuthService authService;
    private final CryptoUtil cryptoUtil;
    private final RsaKeyUtil rsaKeyUtil;


    @GetMapping("/login")
    public String showLoginPage(Model model, HttpServletRequest request) {
        // Генерируем и сохраняем ключ для сессии
        HttpSession session = request.getSession();
        String sessionId = session.getId();

        PublicKey publicKey = rsaKeyUtil.generateAndStoreKeyPair(sessionId);
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        model.addAttribute("publicKey", publicKeyBase64);
        model.addAttribute("loginRequest", new LoginRequest());
        return "auth/login";
    }

    @PostMapping("/login")
    public String processLogin(
            @RequestParam("encryptedEmail") String encryptedEmail,
            @RequestParam("encryptedPassword") String encryptedPassword,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model) {

        try {
            String sessionId = request.getSession().getId();

            // Расшифровываем данные
            String email = cryptoUtil.decryptRsa(encryptedEmail, sessionId);
            String password = cryptoUtil.decryptRsa(encryptedPassword, sessionId);

            // Аутентифицируем пользователя
            String token = authService.authenticate(email, password);

            // Устанавливаем токен в HttpOnly cookie
            Cookie cookie = new Cookie("auth_token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(3600); // 1 час
            response.addCookie(cookie);

            return "redirect:/";

        } catch (Exception e) {
            log.error("Login error", e);
            model.addAttribute("error", "Invalid email or password");

            // Регенерируем ключ для повторной попытки
            HttpSession session = request.getSession();
            String sessionId = session.getId();
            PublicKey publicKey = rsaKeyUtil.generateAndStoreKeyPair(sessionId);
            String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            model.addAttribute("publicKey", publicKeyBase64);

            return "auth/login";
        }
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model, HttpServletRequest request) {
        // Генерируем и сохраняем ключ для сессии
        HttpSession session = request.getSession();
        String sessionId = session.getId();

        PublicKey publicKey = rsaKeyUtil.generateAndStoreKeyPair(sessionId);
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        model.addAttribute("publicKey", publicKeyBase64);
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegistration(
            @RequestParam("encryptedEmail") String encryptedEmail,
            @RequestParam("encryptedPassword") String encryptedPassword,
            HttpServletRequest request,
            Model model) {

        try {
            String sessionId = request.getSession().getId();

            // Расшифровываем данные
            String email = cryptoUtil.decryptRsa(encryptedEmail, sessionId);
            String password = cryptoUtil.decryptRsa(encryptedPassword, sessionId);

            // Создаем RegistrationRequest
            RegistrationRequest registrationRequest = RegistrationRequest.builder()
                    .email(email)
                    .password(password)
                    .build();

            // Регистрируем пользователя
            authService.registerUser(registrationRequest);

            return "redirect:/auth/login?registered";

        } catch (Exception e) {
            log.error("Registration error", e);
            model.addAttribute("error", "Registration failed: " + e.getMessage());

            // Регенерируем ключ
            HttpSession session = request.getSession();
            String sessionId = session.getId();
            PublicKey publicKey = rsaKeyUtil.generateAndStoreKeyPair(sessionId);
            String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            model.addAttribute("publicKey", publicKeyBase64);

            return "auth/register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Удаляем куки с токеном
        Cookie cookie = new Cookie("auth_token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(request.isSecure());
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        // Очищаем сессию
        request.getSession().invalidate();

        return "redirect:/auth/login?logout";
    }
}
