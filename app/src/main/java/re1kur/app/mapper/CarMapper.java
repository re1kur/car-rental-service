package re1kur.app.mapper;

import re1kur.app.core.car.CarReadDto;
import re1kur.app.core.car.CarUpdateDto;
import re1kur.app.core.car.CarWriteDto;
import re1kur.app.entity.Car;

public interface CarMapper {
    Car write(CarWriteDto writeCar);

    CarReadDto read(Car car);

    CarUpdateDto readUpdate(Car car);

    Car update(CarUpdateDto car, int id);

    CarReadDto readWithDetails(Car car);
}
