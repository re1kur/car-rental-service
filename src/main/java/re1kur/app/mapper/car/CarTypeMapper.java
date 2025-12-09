package re1kur.app.mapper.car;

import org.springframework.data.domain.Page;
import re1kur.app.model.dto.CarTypeDto;
import re1kur.app.model.dto.PageDto;
import re1kur.app.model.payload.CarTypePayload;
import re1kur.app.model.payload.CarTypeUpdatePayload;
import re1kur.app.model.entity.CarType;

public interface CarTypeMapper {
    CarType create(CarTypePayload payload);

    CarTypeDto read(CarType carType);

    CarType update(CarType carType, CarTypeUpdatePayload payload);

    PageDto<CarTypeDto> readPage(Page<CarType> page);
}
