package re1kur.app.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import re1kur.app.dto.request.NotificationRequest;
import re1kur.app.dto.request.PushSubscriptionRequest;
import re1kur.app.service.push.PushService;

import java.util.Map;

@Tag(name = "Push", description = "Firebase Cloud Messaging subscriptions and broadcasts")
@RestController
@RequestMapping("/api/v1/push")
@RequiredArgsConstructor
public class PushApiController {

    private final PushService pushService;

    @Operation(summary = "Register an FCM device token (subscribe to push)")
    @PostMapping("/subscriptions")
    public Map<String, Object> subscribe(@Valid @RequestBody PushSubscriptionRequest request,
                                         @AuthenticationPrincipal OidcUser user) {
        pushService.subscribe(request.token(), user != null ? user.getSubject() : null);
        return Map.of("subscribed", true, "enabled", pushService.isEnabled());
    }

    @Operation(summary = "Remove an FCM device token (unsubscribe)")
    @DeleteMapping("/subscriptions")
    public Map<String, Object> unsubscribe(@RequestParam("token") String token) {
        pushService.unsubscribe(token);
        return Map.of("subscribed", false);
    }

    @Operation(summary = "Broadcast a push notification to all subscribers (admin only)")
    @PostMapping("/broadcast")
    public Map<String, Object> broadcast(@Valid @RequestBody NotificationRequest request) {
        int sent = pushService.send(request.title(), request.body(), null);
        return Map.of("sent", sent);
    }
}
