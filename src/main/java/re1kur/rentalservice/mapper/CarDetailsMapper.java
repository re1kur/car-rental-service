package re1kur.rentalservice.mapper;

import re1kur.rentalservice.core.dto.car.details.CarDetailsReadDto;
import re1kur.rentalservice.core.dto.car.details.CarDetailsUpdateDto;
import re1kur.rentalservice.core.dto.car.details.CarDetailsWriteDto;
import re1kur.rentalservice.entity.CarDetails;

public interface CarDetailsMapper {
    CarDetails write(CarDetailsWriteDto details);

    CarDetailsReadDto read(CarDetails details);

    CarDetailsUpdateDto readUpdate(CarDetails details);

    CarDetails update(CarDetailsUpdateDto details, int id);

}
