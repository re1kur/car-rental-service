package re1kur.app.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import re1kur.app.exception.UserEmailAlreadyRegisteredException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakAdminService {

    @Value("${custom.keycloak.server-url}")
    private String serverUrl;
    @Value("${custom.keycloak.realm}")
    private String realm;
    @Value("${custom.keycloak.admin-client-id}")
    private String adminClientId;
    @Value("${custom.keycloak.admin-username}")
    private String adminUsername;
    @Value("${custom.keycloak.admin-password}")
    private String adminPassword;

    private final RestClient rest = RestClient.create();

    public void register(String name, String email, String password) {
        String token = adminToken();
        Map<String, Object> payload = getStringObjectMap(name, email, password);

        rest.post()
                .uri(serverUrl + "/admin/realms/" + realm + "/users")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .exchange((_, response) -> {
                    HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());
                    if (status == HttpStatus.CREATED) {
                        log.info("KEYCLOAK: user [{}] registered", email);
                        return true;
                    }
                    if (status == HttpStatus.CONFLICT) {
                        throw new UserEmailAlreadyRegisteredException(
                                "User with email [%s] is already registered.".formatted(email));
                    }
                    log.error("KEYCLOAK: register failed [{}]", status);
                    throw new IllegalStateException("Keycloak register failed: " + status);
                });
    }

    private static @NonNull Map<String, Object> getStringObjectMap(String name, String email, String password) {
        String[] parts = name.trim().split("\\s+", 2);
        String firstName = parts[0];
        String lastName = parts.length > 1 ? parts[1] : parts[0];

        return Map.of(
                "username", email,
                "email", email,
                "firstName", firstName,
                "lastName", lastName,
                "enabled", true,
                "emailVerified", true,
                "requiredActions", List.of(),
                "credentials", List.of(Map.of(
                        "type", "password",
                        "value", password,
                        "temporary", false
                ))
        );
    }

    @SuppressWarnings("unchecked")
    private String adminToken() {
        String form = "grant_type=password"
                + "&client_id=" + enc(adminClientId)
                + "&username=" + enc(adminUsername)
                + "&password=" + enc(adminPassword);

        Map<String, Object> body = rest.post()
                .uri(serverUrl + "/realms/master/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(Map.class);

        if (body == null || body.get("access_token") == null) {
            throw new IllegalStateException("Keycloak admin token request returned no token");
        }
        return body.get("access_token").toString();
    }

    private String enc(String v) {
        return URLEncoder.encode(v, StandardCharsets.UTF_8);
    }
}
