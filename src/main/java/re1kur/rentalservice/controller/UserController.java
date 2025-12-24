//package re1kur.rentalservice.controller;
//
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Controller;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.ui.Model;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//import re1kur.rentalservice.core.dto.user.RegistrationRequest;
//import re1kur.rentalservice.core.dto.user.UserWriteDto;
//import re1kur.rentalservice.service.AuthService;
//import re1kur.rentalservice.util.CryptoUtil;
//import re1kur.rentalservice.util.RsaKeyUtil;
//
//import java.security.PublicKey;
//import java.util.Base64;
//
//@Slf4j
//@Controller
//@RequestMapping("users")
//@RequiredArgsConstructor
//public class UserController {
//    private final AuthService service;
//    private final CryptoUtil cryptoUtil;
//    private final RsaKeyUtil rsaKeyUtil;
//
//    @GetMapping("register")
//    public String getRegister(Model model) {
//        model.addAttribute("user", new UserWriteDto());
//        // Генерируем и сохраняем ключ для сессии
//        String sessionId = model.asMap().getOrDefault("sessionId", "default").toString();
//        PublicKey publicKey = rsaKeyUtil.generateAndStoreKeyPair(sessionId);
//        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
//        model.addAttribute("publicKey", publicKeyBase64);
//        return "/users/register.html";
//    }
//
//    @Transactional
//    @PostMapping("register")
//    public String register(
//            @Validated @ModelAttribute("user") RegistrationRequest user,
//            @RequestParam("encryptedPassword") String encryptedPassword,
//            @RequestParam("encryptedEmail") String encryptedEmail,
//            HttpServletRequest request) {
//
//        String sessionId = request.getSession().getId();
//
//        try {
//            // Расшифровываем email и пароль
//            String email = cryptoUtil.decryptRsa(encryptedEmail, sessionId);
//            String password = cryptoUtil.decryptRsa(encryptedPassword, sessionId);
//
//            // Устанавливаем расшифрованные данные
//            user.setEmail(email);
//            user.setPassword(password);
//
//            service.registerUser(user);
//            return "redirect:/users/login?registered";
//
//        } catch (Exception e) {
//            log.error("Error during registration", e);
//            return "redirect:/users/register?error=decryption";
//        }
//    }
//
////    @GetMapping("{id}")
////    public String getUser(Model model, @PathVariable int id) {
////        model.addAttribute("user", service.read(id));
////        return "/users/user-info.html";
////    }
//
//    @GetMapping("login")
//    public String getLogin(Model model, HttpServletRequest request) {
//        // Генерируем ключ для сессии
//        String sessionId = request.getSession().getId();
//        PublicKey publicKey = rsaKeyUtil.generateAndStoreKeyPair(sessionId);
//        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
//        model.addAttribute("publicKey", publicKeyBase64);
//        return "/users/login.html";
//    }
//
//    @PostMapping("login")
//    public String login(
//            @RequestParam("encryptedEmail") String encryptedEmail,
//            @RequestParam("encryptedPassword") String encryptedPassword,
//            HttpServletRequest request,
//            HttpServletResponse response) {
//
//        String sessionId = request.getSession().getId();
//
//        try {
//            // Расшифровываем email и пароль
//            String email = cryptoUtil.decryptRsa(encryptedEmail, sessionId);
//            String password = cryptoUtil.decryptRsa(encryptedPassword, sessionId);
//
//            // Аутентифицируем пользователя
//            String token = service.authenticate(email, password);
//
//            // Устанавливаем токен в куки
//            Cookie cookie = new Cookie("auth_token", token);
//            cookie.setHttpOnly(true);
//            cookie.setSecure(request.isSecure());
//            cookie.setPath("/");
//            cookie.setMaxAge(3600);
//            response.addCookie(cookie);
//
//            return "redirect:/dashboard";
//
//        } catch (Exception e) {
//            log.error("Error during login", e);
//            return "redirect:/users/login?error=auth";
//        }
//    }
//
//    @GetMapping("logout")
//    public String logout(HttpServletRequest request, HttpServletResponse response) {
//        // Удаляем куки с токеном
//        Cookie cookie = new Cookie("auth_token", null);
//        cookie.setHttpOnly(true);
//        cookie.setSecure(request.isSecure());
//        cookie.setPath("/");
//        cookie.setMaxAge(0);
//        response.addCookie(cookie);
//
//        // Очищаем сессию
//        request.getSession().invalidate();
//
//        return "redirect:/users/login?logout";
//    }
//}