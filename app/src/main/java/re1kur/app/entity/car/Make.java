package re1kur.app.entity.car;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import re1kur.app.entity.Image;

import java.util.ArrayList;
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

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "title_image_id")
    private Image titleImage;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "make_id")
    private MakeInformation makeInformation;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "make_images",
            joinColumns = @JoinColumn(name = "make_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id"))
    private Collection<Image> makeImages = new ArrayList<>();

    @OneToMany(mappedBy = "make", fetch = FetchType.LAZY)
    private Collection<Car> cars;
}
