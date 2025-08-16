package re1kur.app.mapper;

import re1kur.app.core.dto.CarInformationDto;
import re1kur.app.core.payload.CarPayload;
import re1kur.app.core.payload.CarUpdatePayload;
import re1kur.app.entity.Car;
import re1kur.app.entity.CarInformation;

public interface CarInformationMapper {
    CarInformationDto read(CarInformation information);

    CarInformation update(CarUpdatePayload payload, Car car);

    CarInformation create(CarPayload payload, Car saved);
}
