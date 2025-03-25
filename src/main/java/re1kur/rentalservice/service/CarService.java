package re1kur.rentalservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import re1kur.rentalservice.dto.car.CarReadDto;
import re1kur.rentalservice.dto.car.CarUpdateDto;
import re1kur.rentalservice.dto.car.CarWriteDto;

import java.io.IOException;
import java.util.List;

public interface CarService {

    CarReadDto readByIdWithDetails(int id);

    Integer writeCar(CarWriteDto car) throws IOException;


    List<CarReadDto> readAllByMake(int id);

    CarUpdateDto readUpdateById(int id);

    void updateCar(CarUpdateDto car, int id);


    Page<CarReadDto> readAll(Pageable pageable);
}
