package re1kur.rentalservice.service;

import re1kur.rentalservice.dto.car.CarReadDto;
import re1kur.rentalservice.dto.car.CarUpdateDto;
import re1kur.rentalservice.dto.car.CarWriteDto;

import java.util.List;

public interface CarService {

//    List<CarReadDto> readAllWithDetails();

    CarReadDto readByIdWithDetails(int id);

    Integer writeCar(CarWriteDto car);


    List<CarReadDto> readAllByMake(int id);

    CarUpdateDto readUpdateById(int id);

    void updateCar(CarUpdateDto car, int id);

    List<CarReadDto> readAll();
}
