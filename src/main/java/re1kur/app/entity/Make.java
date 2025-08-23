package re1kur.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "makes")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Make {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "title_image_id")
    private File titleImage;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private MakeInformation information;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "make_images",
            joinColumns = @JoinColumn(name = "make_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id"))
    private List<File> images = new ArrayList<>();

    @OneToMany(mappedBy = "make", fetch = FetchType.LAZY)
    private Collection<Car> cars;

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (!(object instanceof Make make)) return false;
        if (id == null || make.id == null) return false;
        return id.equals(make.id);
    }

    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : System.identityHashCode(this));
    }
}
