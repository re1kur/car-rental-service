package re1kur.rentalservice.mapper;

import re1kur.rentalservice.dto.car.CarReadDto;
import re1kur.rentalservice.dto.car.CarWriteDto;
import re1kur.rentalservice.entity.Car;

public interface CarMapper {
    Car write(CarWriteDto writeCar);

    CarReadDto read(Car car, boolean isInformative, boolean isRender);
}
