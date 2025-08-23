package re1kur.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "files")
@Setter
@Getter
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

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (!(object instanceof File file)) return false;
        if (id == null || file.id == null) return false;
        return id.equals(file.id);
    }

    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : System.identityHashCode(this));
    }
}
