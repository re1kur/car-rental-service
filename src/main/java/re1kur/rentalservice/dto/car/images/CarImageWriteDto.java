package re1kur.rentalservice.dto.car.images;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import re1kur.rentalservice.entity.Car;

@Builder
@Getter
@Setter
public class CarImageWriteDto {
    private Car car;

    @Size(max=256)
    private String url;
}
