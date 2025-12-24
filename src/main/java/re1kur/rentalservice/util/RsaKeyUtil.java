package re1kur.rentalservice.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Base64;

@Component
public class RsaKeyUtil {

    @Autowired
    private CryptoUtil cryptoUtil;

    /**
     * Генерация пары ключей RSA и сохранение приватного ключа
     */
    public PublicKey generateAndStoreKeyPair(String sessionId) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            // Сохраняем приватный ключ
            cryptoUtil.storePrivateKey(sessionId, keyPair.getPrivate());

            return keyPair.getPublic();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating RSA key pair", e);
        }
    }

    /**
     * Конвертация публичного ключа в PEM формат
     */
    public String convertToPem(PublicKey publicKey) {
        byte[] encoded = publicKey.getEncoded();
        String base64 = Base64.getEncoder().encodeToString(encoded);

        return "-----BEGIN PUBLIC KEY-----\n" +
                base64.replaceAll("(.{64})", "$1\n") +
                "\n-----END PUBLIC KEY-----";
    }
}
