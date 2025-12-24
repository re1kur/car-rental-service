package re1kur.rentalservice.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import re1kur.rentalservice.entity.RevokedToken;
import re1kur.rentalservice.repository.RevokedTokenRepository;

import java.util.Date;

@Service
public class TokenBlacklistService {

    @Autowired
    private RevokedTokenRepository repository;

    public void revokeToken(String jti, Date expiresAt, String reason) {
        RevokedToken revokedToken = RevokedToken.builder()
                .jti(jti)
                .expiresAt(expiresAt)
                .revokedAt(new Date())
                .reason(reason)
                .build();
        repository.save(revokedToken);
    }

    public boolean isTokenRevoked(String jti) {
        return repository.existsById(jti);
    }

    // Очистка устаревших токенов
    @Scheduled(cron = "0 0 2 * * ?") // Каждый день в 2:00
    public void cleanupExpiredTokens() {
        repository.deleteByExpiresAtBefore(new Date());
    }
}

