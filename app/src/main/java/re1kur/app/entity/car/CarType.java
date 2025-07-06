package re1kur.app.entity.car;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Table(name = "car_types")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "carType", fetch = FetchType.LAZY)
    private Collection<Car> cars;
}
