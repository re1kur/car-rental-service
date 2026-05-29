package re1kur.app.service.push;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re1kur.app.entity.PushSubscription;
import re1kur.app.repository.push.PushSubscriptionRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PushService {

    private final PushSubscriptionRepository repository;
    private final FirebaseMessaging messaging;

    public PushService(PushSubscriptionRepository repository, ObjectProvider<FirebaseMessaging> messagingProvider) {
        this.repository = repository;
        this.messaging = messagingProvider.getIfAvailable();
    }

    public boolean isEnabled() {
        return messaging != null;
    }

    @Transactional
    public void subscribe(String token, String userId) {
        if (token == null || token.isBlank()) {
            return;
        }
        repository.findByToken(token).ifPresentOrElse(
                existing -> existing.setUserId(userId),
                () -> repository.save(PushSubscription.builder()
                        .token(token)
                        .userId(userId)
                        .createdAt(Instant.now())
                        .build()));
    }

    @Transactional
    public void unsubscribe(String token) {
        if (token != null && !token.isBlank()) {
            repository.deleteByToken(token);
        }
    }

    public int send(String title, String body, String url) {
        if (messaging == null) {
            log.warn("Push send skipped — Firebase not configured.");
            return 0;
        }
        List<PushSubscription> subscriptions = repository.findAll();
        List<PushSubscription> dead = new ArrayList<>();
        int sent = 0;

        for (PushSubscription sub : subscriptions) {
            Message message = Message.builder()
                    .setToken(sub.getToken())
                    .putData("title", title == null ? "" : title)
                    .putData("body", body == null ? "" : body)
                    .putData("url", url == null ? "/" : url)
                    .build();
            try {
                messaging.send(message);
                sent++;
            } catch (FirebaseMessagingException e) {
                if (e.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED
                        || e.getMessagingErrorCode() == MessagingErrorCode.INVALID_ARGUMENT) {
                    dead.add(sub);
                }
                log.warn("Push send failed for subscription [{}]: {}", sub.getId(), e.getMessage());
            }
        }
        if (!dead.isEmpty()) {
            repository.deleteAll(dead);
            log.info("PUSH pruned [{}] dead token(s)", dead.size());
        }
        log.info("PUSH broadcast [{}] -> [{}] of [{}] subscriber(s)", title, sent, subscriptions.size());
        return sent;
    }
}
