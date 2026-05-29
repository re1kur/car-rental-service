package re1kur.app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "custom.firebase")
public class FirebaseProperties {

    /** Path to the Firebase service-account JSON (secret; server-side sending). */
    private String serviceAccount;

    /** Public VAPID key for browser push subscription. */
    private String vapidKey;

    private Web web = new Web();

    @Data
    public static class Web {
        private String apiKey;
        private String authDomain;
        private String projectId;
        private String storageBucket;
        private String messagingSenderId;
        private String appId;
    }
}
