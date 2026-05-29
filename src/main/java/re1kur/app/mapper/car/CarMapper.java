package re1kur.app.mapper.car;

import org.springframework.data.domain.Page;
import re1kur.app.dto.view.CarView;
import re1kur.app.dto.payload.CarUpdatePayload;
import re1kur.app.dto.payload.CarPayload;
import re1kur.app.dto.view.CarFullView;
import re1kur.app.dto.view.CarUpdateView;
import re1kur.app.dto.view.PageView;
import re1kur.app.entity.Car;
import re1kur.app.entity.Make;

public interface CarMapper {
    Car write(CarPayload payload, Make make);

    CarView read(Car car);

    CarUpdateView readUpdate(Car car);

    Car update(Car found, CarUpdatePayload payload, Make make);

    CarFullView readFull(Car car);

    PageView<CarView> readPage(Page<Car> found);
}
