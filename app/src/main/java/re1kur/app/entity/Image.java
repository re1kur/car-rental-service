package re1kur.app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "images")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id
    private String id;

    private String url;

    private OffsetDateTime uploadedAt;

    private OffsetDateTime urlExpiresAt;
}
