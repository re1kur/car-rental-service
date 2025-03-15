package re1kur.rentalservice.dto.car.images;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CarImageWriteDto {
    private int carId;
    private String url;
}
