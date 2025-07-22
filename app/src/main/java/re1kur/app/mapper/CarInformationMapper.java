package re1kur.app.mapper;

import re1kur.app.core.car.details.CarDetailsReadDto;
import re1kur.app.core.car.details.CarDetailsUpdateDto;
import re1kur.app.core.car.details.CarDetailsWriteDto;
import re1kur.app.core.payload.CarPayload;
import re1kur.app.entity.car.Car;
import re1kur.app.entity.car.CarInformation;
import re1kur.app.entity.image.Image;

import java.util.List;

public interface CarInformationMapper {
    CarInformation write(CarDetailsWriteDto details);

    CarDetailsReadDto read(CarInformation details);

    CarDetailsUpdateDto readUpdate(CarInformation details);

    CarInformation update(CarDetailsUpdateDto details, int id);

    CarInformation write(CarPayload payload, Car saved, Image title, List<Image> images);
}
