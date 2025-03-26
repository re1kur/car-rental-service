package re1kur.rentalservice.dto.car.filter;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarFilter {
    private Integer makeId;
    private String model;
    private Integer year;
}
