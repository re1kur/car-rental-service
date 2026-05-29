package re1kur.app.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import re1kur.app.api.dto.MeResponse;
import re1kur.app.api.dto.RegisterRequest;
import re1kur.app.service.KeycloakAdminService;

import java.util.List;
import java.util.Map;

@Tag(name = "Auth", description = "Registration (Keycloak), session info and logout")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthApiController {

    private static final String LOGIN_URL = "/oauth2/authorization/keycloak";

    private final KeycloakAdminService keycloakAdminService;

    @Operation(summary = "Register a new user (created in Keycloak; password hashed by Keycloak)")
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request) {
        keycloakAdminService.register(request.name(), request.email(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "User registered. Use the login URL to sign in.",
                "email", request.email(),
                "loginUrl", LOGIN_URL
        ));
    }

    @Operation(summary = "Login entry point (OAuth2 / Keycloak authorization-code flow)")
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@AuthenticationPrincipal OidcUser user) {
        if (user != null) {
            return ResponseEntity.ok(Map.of(
                    "message", "Already authenticated.",
                    "subject", user.getSubject()
            ));
        }
        return ResponseEntity.ok(Map.of(
                "message", "Authentication is handled by Keycloak. Open the login URL to sign in.",
                "loginUrl", LOGIN_URL
        ));
    }

    @Operation(summary = "Logout — invalidate the current session")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get the currently authenticated user")
    @GetMapping("/me")
    public MeResponse me(@AuthenticationPrincipal OidcUser user) {
        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return new MeResponse(
                user.getSubject(),
                user.getPreferredUsername(),
                user.getEmail(),
                user.getFullName() != null ? user.getFullName() : user.getClaimAsString("name"),
                roles,
                roles.contains("ROLE_ADMIN")
        );
    }
}
