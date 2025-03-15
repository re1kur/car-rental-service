package re1kur.rentalservice.service;

import org.springframework.web.bind.annotation.PathVariable;
import re1kur.rentalservice.dto.car.CarReadDto;
import re1kur.rentalservice.dto.car.CarWriteDto;

import java.util.List;

public interface CarService {

    List<CarReadDto> findAll(boolean isInformative, boolean isRender);

    CarReadDto findById(int id, boolean isInformative, boolean isRender);

    CarReadDto writeCar(CarWriteDto newCar);
}
