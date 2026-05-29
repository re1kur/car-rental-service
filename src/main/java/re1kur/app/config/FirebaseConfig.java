package re1kur.app.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.InputStream;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FirebaseConfig {

    private final FirebaseProperties properties;

    // Returns null (and push stays disabled) when no service account is configured,
    // so the app boots fine without Firebase credentials. PushService injects this optionally.
    @Bean
    public FirebaseMessaging firebaseMessaging() {
        String path = properties.getServiceAccount();
        if (path == null || path.isBlank()) {
            log.warn("Firebase service-account not configured (custom.firebase.service-account) — push disabled.");
            return null;
        }
        try (InputStream in = new FileInputStream(path)) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(in))
                    .build();
            FirebaseApp app = FirebaseApp.getApps().isEmpty()
                    ? FirebaseApp.initializeApp(options)
                    : FirebaseApp.getInstance();
            log.info("Firebase initialized — push enabled.");
            return FirebaseMessaging.getInstance(app);
        } catch (Exception e) {
            log.error("Failed to initialize Firebase ({}) — push disabled.", e.getMessage());
            return null;
        }
    }
}
