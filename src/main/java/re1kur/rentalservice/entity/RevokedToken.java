package re1kur.rentalservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "revoked_tokens")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevokedToken {
    @Id
    private String jti; // JWT ID
    private Date expiresAt;
    private Date revokedAt;
    private String reason; // "stolen", "logout", "compromised"
}
