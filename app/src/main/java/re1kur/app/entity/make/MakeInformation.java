package re1kur.app.entity.make;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "make_information")
@Data
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
}
