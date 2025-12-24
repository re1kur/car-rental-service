//package re1kur.rentalservice.controller;
//
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//import re1kur.rentalservice.core.dto.user.AuthResponse;
//import re1kur.rentalservice.core.dto.user.LoginRequest;
//import re1kur.rentalservice.core.dto.user.RegistrationRequest;
//import re1kur.rentalservice.core.dto.user.UserProfile;
//import re1kur.rentalservice.core.exception.AuthenticationException;
//import re1kur.rentalservice.entity.Role;
//import re1kur.rentalservice.entity.User;
//import re1kur.rentalservice.repository.RoleRepository;
//import re1kur.rentalservice.repository.UserRepository;
//import re1kur.rentalservice.service.AuthService;
//import re1kur.rentalservice.util.DeviceFingerprintUtil;
//import re1kur.rentalservice.util.JwtUtil;
//import re1kur.rentalservice.util.PasswordUtil;
//
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api/auth")
//@RequiredArgsConstructor
//@Validated
//public class AuthController {
//    private final AuthService authService;
//    private final PasswordUtil passwordUtil;
//    private final JwtUtil jwtUtil;
//    private final DeviceFingerprintUtil fingerprintUtil;
//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
//
//
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest request, HttpServletRequest httpRequest) {
//        // Проверяем совпадение паролей
//        if (!request.getPassword().equals(request.getConfirmPassword())) {
//            return ResponseEntity.badRequest().body("Passwords do not match");
//        }
//
//        // Проверяем сложность пароля
//        if (!passwordUtil.isPasswordStrong(request.getPassword())) {
//            return ResponseEntity.badRequest().body(
//                    "Password must contain at least 8 characters, " +
//                            "uppercase and lowercase letters, numbers and special characters"
//            );
//        }
//
//        User user = authService.registerUser(request);
//
//        // Генерируем токены
//        Set<String> roles = user.getRoles().stream()
//                .map(Role::getName)
//                .collect(Collectors.toSet());
//        String fingerprint = fingerprintUtil.generateFingerprint(httpRequest);
////        String accessToken = jwtUtil.generateToken(
////                user.getEmail(),
////                roles,
////                fingerprint,
////                httpRequest.getRemoteAddr()
////        );
//        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());
//
//        AuthResponse response = AuthResponse.builder()
////                .accessToken(accessToken)
////                .refreshToken(refreshToken)
//                .expiresIn(3600L)
//                .user(UserProfile.builder()
//                        .id(user.getId())
//                        .email(user.getEmail())
//                        .roles(roles)
//                        .build())
//                .build();
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request,
//                                   HttpServletResponse response) {
//        try {
//            String accessToken = authService.authenticate(request.getEmail(), request.getPassword(), );
//
//            // Получаем пользователя для информации
//            User user = userRepository.findByEmail(request.getEmail())
//                    .orElseThrow(() -> new AuthenticationException("User not found"));
//
//            Set<String> roles = user.getRoles().stream()
//                    .map(Role::getName)
//                    .collect(Collectors.toSet());
//
//            String refreshToken = jwtUtil.generateRefreshToken(request.getEmail());
//
//            // Устанавливаем HttpOnly куки
//            setSecureCookies(response, accessToken, refreshToken);
//
//            AuthResponse authResponse = AuthResponse.builder()
//                    .accessToken(accessToken)
//                    .refreshToken(refreshToken)
//                    .expiresIn(3600L)
//                    .user(UserProfile.builder()
//                            .id(user.getId())
//                            .email(user.getEmail())
//                            .roles(roles)
//                            .build())
//                    .build();
//
//            return ResponseEntity.ok(authResponse);
//
//        } catch (AuthenticationException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("Invalid email or password");
//        }
//    }
//
//    @PostMapping("/refresh")
//    public ResponseEntity<?> refreshToken(@CookieValue(value = "refresh_token", required = false) String refreshToken) {
//        if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
//        }
//
//        if (!"refresh".equals(jwtUtil.extractTokenType(refreshToken))) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token type");
//        }
//
//        String email = jwtUtil.extractEmail(refreshToken);
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new AuthenticationException("User not found"));
//
//        Set<String> roles = user.getRoles().stream()
//                .map(Role::getName)
//                .collect(Collectors.toSet());
//
//        String newAccessToken = jwtUtil.generateToken(email, roles);
//        String newRefreshToken = jwtUtil.generateRefreshToken(email);
//
//        return ResponseEntity.ok(AuthResponse.builder()
//                .accessToken(newAccessToken)
//                .refreshToken(newRefreshToken)
//                .expiresIn(3600L)
//                .build());
//    }
//
//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(HttpServletResponse response) {
//        // Очищаем куки
//        clearSecureCookies(response);
//        return ResponseEntity.ok("Logged out successfully");
//    }
//
//    @GetMapping("/me")
//    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
//        String token = authHeader.substring(7); // Убираем "Bearer "
//        String email = jwtUtil.extractEmail(token);
//
//        UserProfile profile = authService.getCurrentUserProfile(email);
//        return ResponseEntity.ok(profile);
//    }
//
//    private void setSecureCookies(HttpServletResponse response, String accessToken, String refreshToken) {
//        // Access token cookie (короткоживущий)
//        Cookie accessCookie = new Cookie("access_token", accessToken);
//        accessCookie.setHttpOnly(true);
//        accessCookie.setSecure(true); // Только для HTTPS
//        accessCookie.setPath("/");
//        accessCookie.setMaxAge(3600); // 1 час
//        accessCookie.setAttribute("SameSite", "Strict");
//        response.addCookie(accessCookie);
//
//        // Refresh token cookie (долгоживущий)
//        Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
//        refreshCookie.setHttpOnly(true);
//        refreshCookie.setSecure(true);
//        refreshCookie.setPath("/api/auth/refresh");
//        refreshCookie.setMaxAge(86400); // 24 часа
//        refreshCookie.setAttribute("SameSite", "Strict");
//        response.addCookie(refreshCookie);
//    }
//
//    private void clearSecureCookies(HttpServletResponse response) {
//        Cookie accessCookie = new Cookie("access_token", null);
//        accessCookie.setHttpOnly(true);
//        accessCookie.setSecure(true);
//        accessCookie.setPath("/");
//        accessCookie.setMaxAge(0);
//        response.addCookie(accessCookie);
//
//        Cookie refreshCookie = new Cookie("refresh_token", null);
//        refreshCookie.setHttpOnly(true);
//        refreshCookie.setSecure(true);
//        refreshCookie.setPath("/api/auth/refresh");
//        refreshCookie.setMaxAge(0);
//        response.addCookie(refreshCookie);
//    }
//}