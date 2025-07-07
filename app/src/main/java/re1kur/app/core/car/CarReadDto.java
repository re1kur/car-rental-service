package re1kur.app.core.car;

import lombok.Builder;
import lombok.Data;
import re1kur.app.core.car.details.CarDetailsReadDto;
import re1kur.app.core.car.images.CarImageReadDto;
import re1kur.app.core.dto.MakeDto;

import java.util.List;

@Builder
@Data
public class CarReadDto {
    private int id;
    private MakeDto make;
    private String model;
    private int year;
    private String licensePlate;
    private CarImageReadDto titleImage;
    boolean isAvailable;
    private CarDetailsReadDto details;
    private List<CarImageReadDto> images;
}
