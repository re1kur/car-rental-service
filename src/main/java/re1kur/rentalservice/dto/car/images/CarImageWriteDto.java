package re1kur.rentalservice.dto.car.images;

import lombok.Builder;
import lombok.Getter;
import re1kur.rentalservice.entity.Car;

@Builder
@Getter
public class CarImageWriteDto {
    private Car car;

    private String url;
}
