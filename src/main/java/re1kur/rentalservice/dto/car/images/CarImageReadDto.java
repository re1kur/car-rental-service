package re1kur.rentalservice.dto.car.images;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Builder
@Value
public class CarImageReadDto {
    int id;
    int carId;
    String url;
    LocalDateTime uploadedAt;
}
