package re1kur.fs.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

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
    private ZonedDateTime uploadedAt;

    private ZonedDateTime urlExpiresAt;
}
