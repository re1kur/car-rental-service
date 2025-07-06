package re1kur.app.mapper;

import re1kur.app.core.dto.CarTypeDto;
import re1kur.app.core.payload.CarTypePayload;
import re1kur.app.core.payload.CarTypeUpdatePayload;
import re1kur.app.entity.car.CarType;

public interface CarTypeMapper {
    CarType create(CarTypePayload payload);

    CarTypeDto read(CarType carType);

    CarType update(CarType carType, CarTypeUpdatePayload payload);
}
