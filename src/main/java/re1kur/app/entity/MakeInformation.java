package re1kur.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "make_information")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "make")
public class MakeInformation {
    @Id
    private Integer makeId;

    @MapsId
    @OneToOne(mappedBy = "information")
    @JoinColumn(name = "make_id")
    private Make make;

    private String country;

    private String description;

    private LocalDate foundedAt;

    private String founder;

    private String owner;

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (!(object instanceof MakeInformation makeInfo)) return false;
        if (makeId == null || makeInfo.makeId == null) return false;
        return makeId.equals(makeInfo.makeId);
    }

    @Override
    public int hashCode() {
        return (makeId != null ? makeId.hashCode() : System.identityHashCode(this));
    }
}
