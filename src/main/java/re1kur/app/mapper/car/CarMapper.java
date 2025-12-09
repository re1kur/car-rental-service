package re1kur.app.mapper.car;

import org.springframework.data.domain.Page;
import re1kur.app.model.dto.CarDto;
import re1kur.app.model.payload.CarUpdatePayload;
import re1kur.app.model.payload.CarPayload;
import re1kur.app.model.dto.CarFullDto;
import re1kur.app.model.dto.CarUpdateDto;
import re1kur.app.model.dto.PageDto;
import re1kur.app.model.entity.Car;
import re1kur.app.model.entity.CarType;
import re1kur.app.model.entity.Engine;
import re1kur.app.model.entity.Make;

public interface CarMapper {
    Car write(CarPayload payload, Make make, CarType type, Engine engine);

    CarDto read(Car car);

    CarUpdateDto readUpdate(Car car);

    Car update(Car found, CarUpdatePayload payload, Make make, CarType type, Engine engine);

    CarFullDto readFull(Car car);

    PageDto<CarDto> readPage(Page<Car> found);
}
