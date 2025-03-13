package re1kur.rentalservice.dto.car;

import lombok.Builder;
import lombok.Value;
import re1kur.rentalservice.dto.car.details.CarDetailsReadDto;
import re1kur.rentalservice.dto.car.images.CarImageReadDto;

import java.util.List;

@Value
@Builder
public class CarReadDto {
    int id;
    String make;
    String model;
    int year;
    String licensePlate;
    boolean isAvailable;
    CarDetailsReadDto details;
    List<CarImageReadDto> images;
}
