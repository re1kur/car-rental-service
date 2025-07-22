package re1kur.app.mapper.impl;

import org.springframework.stereotype.Component;
import re1kur.app.core.dto.CarTypeDto;
import re1kur.app.core.payload.CarTypePayload;
import re1kur.app.core.payload.CarTypeUpdatePayload;
import re1kur.app.entity.cartype.CarType;
import re1kur.app.mapper.CarTypeMapper;

@Component
public class CarTypeMapperImpl implements CarTypeMapper {
    @Override
    public CarType create(CarTypePayload payload) {
        return CarType.builder()
                .name(payload.name())
                .build();
    }

    @Override
    public CarTypeDto read(CarType carType) {
        return CarTypeDto.builder()
                .id(carType.getId())
                .name(carType.getName())
                .build();
    }

    @Override
    public CarType update(CarType carType, CarTypeUpdatePayload payload) {
        carType.setName(payload.name());
        return carType;
    }
}
