package re1kur.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "car_information")
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "car")
public class CarInformation {
    @Id
    private Integer carId;

    @MapsId
    @OneToOne(mappedBy = "information")
    private Car car;

    private String description;

    private String color;

    private Integer seats;

    private Integer mileage;

    private String fuelType;

    private String transmission;

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (!(object instanceof CarInformation carInfo)) return false;
        if (carId == null || carInfo.carId == null) return false;
        return carId.equals(carInfo.car);
    }

    @Override
    public int hashCode() {
        return (carId != null ? carId.hashCode() : System.identityHashCode(this));
    }
}
