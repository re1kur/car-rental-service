package re1kur.rentalservice.util;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class CryptoUtil {

    private final Map<String, PrivateKey> privateKeys = new ConcurrentHashMap<>();

    /**
     * Расшифровка RSA
     */
    public String decryptRsa(String encryptedData, String sessionId) throws Exception {
        PrivateKey privateKey = privateKeys.get(sessionId);
        if (privateKey == null) {
            throw new IllegalArgumentException("Private key not found for session: " + sessionId);
        }

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    /**
     * Сохранение приватного ключа
     */
    public void storePrivateKey(String sessionId, PrivateKey privateKey) {
        privateKeys.put(sessionId, privateKey);

        // Автоочистка через 10 минут
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> privateKeys.remove(sessionId), 10, TimeUnit.MINUTES);
    }
}