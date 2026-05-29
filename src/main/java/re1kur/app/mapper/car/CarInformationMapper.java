package re1kur.app.mapper.car;

import re1kur.app.dto.view.CarDetailsView;
import re1kur.app.dto.payload.CarPayload;
import re1kur.app.dto.payload.CarUpdatePayload;
import re1kur.app.entity.Car;
import re1kur.app.entity.CarInformation;

public interface CarInformationMapper {
    CarDetailsView read(CarInformation information);

    CarInformation update(CarUpdatePayload payload, Car car);

    CarInformation create(CarPayload payload, Car saved);
}
