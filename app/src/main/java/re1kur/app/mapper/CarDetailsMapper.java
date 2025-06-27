package re1kur.app.mapper;

import re1kur.app.dto.car.details.CarDetailsReadDto;
import re1kur.app.dto.car.details.CarDetailsUpdateDto;
import re1kur.app.dto.car.details.CarDetailsWriteDto;
import re1kur.app.entity.CarDetails;

public interface CarDetailsMapper {
    CarDetails write(CarDetailsWriteDto details);

    CarDetailsReadDto read(CarDetails details);

    CarDetailsUpdateDto readUpdate(CarDetails details);

    CarDetails update(CarDetailsUpdateDto details, int id);

}
