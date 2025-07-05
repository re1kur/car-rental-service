package re1kur.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "car_details")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "car")
public class CarDetails {
    @Id
    private Integer carId;

    @OneToOne(mappedBy = "details")
    private Car car;

    private String description;

    private String color;

    private Integer seats;

    private Integer mileage;

    private String fuelType;

    private String transmission;
}
