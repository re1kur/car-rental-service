package re1kur.app.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "car_id")
    private CarDetails details;
}


