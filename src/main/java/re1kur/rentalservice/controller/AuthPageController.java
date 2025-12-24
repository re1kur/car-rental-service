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
import re1kur.rentalservice.service.TokenBlacklistService;
import re1kur.rentalservice.util.CryptoUtil;
import re1kur.rentalservice.util.DeviceFingerprintUtil;
import re1kur.rentalservice.util.JwtUtil;
import re1kur.rentalservice.util.RsaKeyUtil;

import java.security.PublicKey;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthPageController {
    private final AuthService authService;
    private final CryptoUtil cryptoUtil;
    private final RsaKeyUtil rsaKeyUtil;
    private final DeviceFingerprintUtil fingerprintUtil;
    private final JwtUtil jwtUtil;
    private final TokenBlacklistService tokenBlacklistService;


    @GetMapping("/login")
    public String showLoginPage(Model model, HttpServletRequest request) {
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

            String email = cryptoUtil.decryptRsa(encryptedEmail, sessionId);
            String password = cryptoUtil.decryptRsa(encryptedPassword, sessionId);

            String token = authService.authenticate(email, password, fingerprintUtil.generateFingerprint(request));
            String refreshToken = authService.getRefresh(email);

            Cookie cookie = new Cookie("auth_token", token);
            cookie.setAttribute("refresh_token", refreshToken);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(3600); // 1 час
            response.addCookie(cookie);

            return "redirect:/";

        } catch (Exception e) {
            log.error("Login error", e);
            model.addAttribute("error", "Invalid email or password");

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

            String email = cryptoUtil.decryptRsa(encryptedEmail, sessionId);
            String password = cryptoUtil.decryptRsa(encryptedPassword, sessionId);

            RegistrationRequest registrationRequest = RegistrationRequest.builder()
                    .email(email)
                    .password(password)
                    .build();

            authService.registerUser(registrationRequest);

            return "redirect:/auth/login?registered";

        } catch (Exception e) {
            log.error("Registration error", e);
            model.addAttribute("error", "Registration failed: " + e.getMessage());

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
        String token = extractTokenFromCookie(request);
        if (token != null && jwtUtil.validateToken(token)) {
            String jti = jwtUtil.extractJti(token);
            Date expiresAt = jwtUtil.extractExpiration(token);
            tokenBlacklistService.revokeToken(jti, expiresAt, "user_logout");
            log.info("Token revoked for logout: {}", jti);
        }

        String sessionId = request.getSession().getId();
        cryptoUtil.removePrivateKey(sessionId);

        Cookie cookie = new Cookie("auth_token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(request.isSecure());
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        request.getSession().invalidate();
        return "redirect:/auth/login?logout";
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("auth_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
