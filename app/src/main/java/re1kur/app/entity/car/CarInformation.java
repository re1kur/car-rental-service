package re1kur.app.entity.car;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "car_information")
@Builder
@Data
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
}
