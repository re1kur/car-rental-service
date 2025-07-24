package re1kur.app.mapper;

import org.springframework.data.domain.Page;
import re1kur.app.core.dto.CarDto;
import re1kur.app.core.car.CarUpdateDto;
import re1kur.app.core.dto.*;
import re1kur.app.core.payload.CarPayload;
import re1kur.app.entity.car.Car;
import re1kur.app.entity.cartype.CarType;
import re1kur.app.entity.engine.Engine;
import re1kur.app.entity.make.Make;

public interface CarMapper {
    Car write(CarPayload payload, Make make, CarType type, Engine engine);

    CarDto read(Car car);

    CarUpdateDto readUpdate(Car car);

    Car update(CarUpdateDto car, int id);

    CarFullDto readFull(Car car);

    PageDto<CarDto> readPage(Page<Car> found);
}
