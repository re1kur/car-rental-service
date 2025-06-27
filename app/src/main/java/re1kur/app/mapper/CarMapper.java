package re1kur.app.mapper;

import re1kur.app.dto.car.CarReadDto;
import re1kur.app.dto.car.CarUpdateDto;
import re1kur.app.dto.car.CarWriteDto;
import re1kur.app.entity.Car;

public interface CarMapper {
    Car write(CarWriteDto writeCar);

    CarReadDto read(Car car);

    CarUpdateDto readUpdate(Car car);

    Car update(CarUpdateDto car, int id);

    CarReadDto readWithDetails(Car car);
}
