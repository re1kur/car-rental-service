package re1kur.app.mapper;

import org.springframework.data.domain.Page;
import re1kur.app.core.dto.CarDto;
import re1kur.app.core.payload.CarUpdatePayload;
import re1kur.app.core.dto.*;
import re1kur.app.core.payload.CarPayload;
import re1kur.app.entity.Car;
import re1kur.app.entity.Make;

public interface CarMapper {
    Car write(CarPayload payload, Make make);

    CarDto read(Car car);

    CarUpdateDto readUpdate(Car car);

    Car update(Car found, CarUpdatePayload payload, Make make);

    CarFullDto readFull(Car car);

    PageDto<CarDto> readPage(Page<Car> found);
}
