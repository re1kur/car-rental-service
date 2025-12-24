package re1kur.rentalservice.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

@Component
public class DeviceFingerprintUtil {

    public String generateFingerprint(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        String acceptLanguage = request.getHeader("Accept-Language");

        String data = ip + "|" + userAgent + "|" + acceptLanguage;
        return DigestUtils.md5DigestAsHex(data.getBytes());
    }

    public boolean validateFingerprint(String tokenFingerprint, HttpServletRequest request) {
        String currentFingerprint = generateFingerprint(request);
        return tokenFingerprint.equals(currentFingerprint);
    }
}
