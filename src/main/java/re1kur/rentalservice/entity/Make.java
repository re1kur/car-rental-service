package re1kur.rentalservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Table(name = "makes")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Make {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String country;

    private String description;

    private String titleImageUrl;

    @OneToMany(mappedBy = "make", fetch = FetchType.LAZY)
    private Collection<Car> cars;
}
