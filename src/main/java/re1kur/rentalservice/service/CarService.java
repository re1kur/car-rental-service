package re1kur.rentalservice.service;

import re1kur.rentalservice.dto.car.CarReadDto;
import re1kur.rentalservice.dto.car.CarUpdateDto;
import re1kur.rentalservice.dto.car.CarWriteDto;
import re1kur.rentalservice.dto.car.details.CarDetailsWriteDto;

import java.util.List;

public interface CarService {

    List<CarReadDto> readAll(boolean isInformative, boolean isRender);

    CarReadDto readById(int id, boolean isInformative, boolean isRender);

    Integer writeCar(CarWriteDto car);


    List<CarReadDto> readAllByMake(int id, boolean isInformative, boolean isRender);

//    void writeCarDetails(CarDetailsWriteDto carDetails, int id);

    CarUpdateDto readUpdateById(int id);

    void updateCar(CarUpdateDto car, int id);

}
