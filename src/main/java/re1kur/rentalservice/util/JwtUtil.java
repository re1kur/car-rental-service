package re1kur.rentalservice.util;


import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.*;

@Component
public class JwtUtil {

    private final String SECRET_KEY;
    private final long EXPIRATION_TIME = 3600000; // 1 час
    private final long REFRESH_EXPIRATION_TIME = 86400000; // 24 часа

    public JwtUtil() {
        // Получаем секретный ключ из переменных окружения
        this.SECRET_KEY = Optional.ofNullable(System.getenv("JWT_SECRET"))
                .orElseGet(() -> {
                    // Генерируем безопасный ключ для разработки
                    byte[] key = new byte[256];
                    new SecureRandom().nextBytes(key);
                    return Base64.getEncoder().encodeToString(key);
                });
    }

    /**
     * Генерация access token
     */
    public String generateToken(String email, Set<String> roles, String fingerprint) {
        String jti = UUID.randomUUID().toString();
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("roles", new ArrayList<>(roles));
        claims.put("type", "access");
        claims.put("fingerprint", fingerprint);
        claims.put("jti", jti);

        return Jwts.builder()
                .setId(jti)
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * Генерация refresh token
     */
    public String generateRefreshToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("type", "refresh");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * Валидация токена
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Извлечение email из токена
     */
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Извлечение username из токена
     */
    public String extractJti(String token) {
        return extractAllClaims(token).get("jti", String.class);
    }

    /**
     * Извлечение ролей из токена
     */
    public Set<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        @SuppressWarnings("unchecked")
        List<String> roles = claims.get("roles", List.class);
        return roles != null ? new HashSet<>(roles) : new HashSet<>();
    }

    /**
     * Проверка типа токена
     */
    public String extractTokenType(String token) {
        return extractAllClaims(token).get("type", String.class);
    }

    /**
     * Извлечение всех claims из токена
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Проверка, истек ли срок действия токена
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = extractAllClaims(token).getExpiration();
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public String extractFingerprint(String token) {
        return extractAllClaims(token).get("fingerprint", String.class);
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).get("exp", Date.class);
    }
}