package re1kur.app.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import re1kur.app.dto.request.NotificationRequest;
import re1kur.app.service.websocket.notification.NotificationService;

import java.util.Map;

@Tag(name = "Notifications", description = "Broadcast push notifications to subscribers (admin only)")
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationApiController {

    private final NotificationService notificationService;

    @Operation(summary = "Broadcast a notification to all subscribed clients")
    @PostMapping
    public Map<String, Object> broadcast(@Valid @RequestBody NotificationRequest request) {
        int sent = notificationService.broadcast(request.title(), request.body(), null);
        return Map.of("sent", sent);
    }
}
