package re1kur.rentalservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "car_details")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CarDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private Car car;

    private String color;

    private Integer mileage;

    private String fuelType;

    private String transmission;
}
