package re1kur.app.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @OneToOne(mappedBy = "makeInformation")
    @JoinColumn(name = "make_id")
    private Make make;

    private String country;

    private String description;

    private Integer foundedAt;

    private String founder;

    private String owner;
}
