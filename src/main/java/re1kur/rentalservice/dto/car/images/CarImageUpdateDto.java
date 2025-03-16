package re1kur.rentalservice.dto.car.images;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import re1kur.rentalservice.dto.car.CarUpdateDto;

@Builder
@Getter
@Setter
public class CarImageUpdateDto {
    private int id;

    private CarUpdateDto car;

    @Size(max=256)
    private String url;
}
