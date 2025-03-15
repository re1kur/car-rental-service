package re1kur.rentalservice.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Entity
@Table(name = "cars")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Make make;

    private String model;

    private Integer year;

    private String licensePlate;

    @Column(insertable = false)
    private boolean isAvailable;

    @OneToOne(mappedBy = "car", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private CarDetails details;

    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Collection<CarImage> images;

}


