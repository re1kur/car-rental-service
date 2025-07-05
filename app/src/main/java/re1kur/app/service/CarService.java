package re1kur.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import re1kur.app.core.car.CarReadDto;
import re1kur.app.core.car.CarUpdateDto;
import re1kur.app.core.car.CarWriteDto;
import re1kur.app.core.car.filter.CarFilter;

import java.io.IOException;

public interface CarService {

    CarReadDto readByIdWithDetails(int id);

    Integer writeCar(CarWriteDto car) throws IOException;

    CarUpdateDto readUpdateById(int id);

    void updateCar(CarUpdateDto car, int id);

    Page<CarReadDto> readAll(CarFilter filter, Pageable pageable);
}
