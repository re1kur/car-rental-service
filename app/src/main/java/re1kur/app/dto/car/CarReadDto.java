package re1kur.app.dto.car;

import lombok.Builder;
import lombok.Data;
import re1kur.app.dto.car.details.CarDetailsReadDto;
import re1kur.app.dto.car.images.CarImageReadDto;
import re1kur.app.dto.make.MakeReadDto;

import java.util.List;

@Builder
@Data
public class CarReadDto {
    private int id;
    private MakeReadDto make;
    private String model;
    private int year;
    private String licensePlate;
    private CarImageReadDto titleImage;
    boolean isAvailable;
    private CarDetailsReadDto details;
    private List<CarImageReadDto> images;
}
