package re1kur.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "files")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class File {
    @Id
    private String id;

    private String mediaType;

    private String url;

    @Column(insertable = false, updatable = false)
    private Instant uploadedAt;

    private Instant urlExpiresAt;
}
