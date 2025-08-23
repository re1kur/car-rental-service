package re1kur.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Entity
@Table(name = "car_types")
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CarType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "carType", fetch = FetchType.LAZY)
    private Collection<Car> cars;

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (!(object instanceof CarType carType)) return false;
        if (id == null || carType.id == null) return false;
        return id.equals(carType.id);
    }

    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : System.identityHashCode(this));
    }
}
