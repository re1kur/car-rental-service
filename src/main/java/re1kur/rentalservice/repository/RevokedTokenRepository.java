package re1kur.rentalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import re1kur.rentalservice.entity.RevokedToken;

import java.util.Date;

public interface RevokedTokenRepository extends JpaRepository<RevokedToken, String> {
    void deleteByExpiresAtBefore(Date date);
}
