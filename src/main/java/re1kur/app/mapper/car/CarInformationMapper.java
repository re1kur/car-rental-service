package re1kur.app.mapper.car;

import re1kur.app.model.dto.CarInformationDto;
import re1kur.app.model.payload.CarPayload;
import re1kur.app.model.payload.CarUpdatePayload;
import re1kur.app.model.entity.Car;
import re1kur.app.model.entity.CarInformation;

public interface CarInformationMapper {
    CarInformationDto read(CarInformation information);

    CarInformation update(CarUpdatePayload payload, Car car);

    CarInformation create(CarPayload payload, Car saved);
}
