package re1kur.app.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;


@Entity
@Table(name = "cars")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id")
    private CarInformation information;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "make_id")
    private Make make;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_type_id")
    private CarType carType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "engine_id")
    private Engine engine;

    private String model;

    private Integer year;

    private String licensePlate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "title_image_id")
    private File titleImage;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "car_images",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id"))
    private Collection<File> images;

    @Column(insertable = false, columnDefinition = "DEFAULT FALSE")
    private boolean isAvailable;

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (!(object instanceof Car car)) return false;
        if (id == null || car.id == null) return false;
        return id.equals(car.id);
    }

    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : System.identityHashCode(this));
    }
}