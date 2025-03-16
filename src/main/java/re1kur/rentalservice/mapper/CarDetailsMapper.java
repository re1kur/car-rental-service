package re1kur.rentalservice.mapper;

import re1kur.rentalservice.dto.car.details.CarDetailsReadDto;
import re1kur.rentalservice.dto.car.details.CarDetailsUpdateDto;
import re1kur.rentalservice.dto.car.details.CarDetailsWriteDto;
import re1kur.rentalservice.entity.CarDetails;

public interface CarDetailsMapper {
    CarDetails write(CarDetailsWriteDto writeCarDetails);

    CarDetailsReadDto read(CarDetails details);

    CarDetails update(CarDetailsUpdateDto details);
}
