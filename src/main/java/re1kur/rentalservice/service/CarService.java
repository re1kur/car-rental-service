package re1kur.rentalservice.service;

import re1kur.rentalservice.dto.car.CarReadDto;
import re1kur.rentalservice.dto.car.CarUpdateDto;
import re1kur.rentalservice.dto.car.CarWriteDto;

import java.util.List;

public interface CarService {

    List<CarReadDto> readAll(boolean isInformative, boolean isRender);

    CarReadDto readById(int id, boolean isInformative, boolean isRender);

    CarReadDto writeCar(CarWriteDto newCar);

    CarReadDto updateCar(CarUpdateDto update);

    List<CarReadDto> readAllByMake(int id, boolean isInformative, boolean isRender);
}
