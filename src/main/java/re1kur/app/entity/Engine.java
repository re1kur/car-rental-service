package re1kur.app.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Entity
@Table(name = "engines")
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Engine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "engine", fetch = FetchType.LAZY)
    private Collection<Car> cars;

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (!(object instanceof Engine engine)) return false;
        if (id == null || engine.id == null) return false;
        return id.equals(engine.id);
    }

    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : System.identityHashCode(this));
    }
}
