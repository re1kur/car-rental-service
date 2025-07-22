package re1kur.app.entity.car;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import re1kur.app.entity.image.Image;
import re1kur.app.entity.cartype.CarType;
import re1kur.app.entity.engine.Engine;
import re1kur.app.entity.make.Make;

import java.util.Collection;


@Entity
@Table(name = "cars")
@Builder
@Data
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
    private Image titleImage;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "car_images",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id"))
    private Collection<Image> images;

    @Column(insertable = false, columnDefinition = "DEFAULT FALSE")
    private boolean isAvailable;

}


