package re1kur.app.entity.engine;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import re1kur.app.entity.car.Car;

import java.util.Collection;

@Entity
@Table(name = "engines")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Engine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "engine", fetch = FetchType.LAZY)
    private Collection<Car> cars;
}
