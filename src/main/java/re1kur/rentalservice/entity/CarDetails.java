package re1kur.rentalservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "car_details")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "car_id")
    private Car car;

    private String color;

    private Integer mileage;

    private String fuelType;

    private String transmission;
}
