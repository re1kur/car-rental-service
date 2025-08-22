package re1kur.app.service;

import org.springframework.data.domain.Pageable;
import re1kur.app.core.dto.CarTypeDto;
import re1kur.app.core.dto.PageDto;
import re1kur.app.core.payload.CarTypePayload;
import re1kur.app.core.payload.CarTypeUpdatePayload;
import re1kur.app.entity.CarType;

import java.util.List;

public interface CarTypeService {
    void create(CarTypePayload payload);

    CarTypeDto read(Integer id);

    void update(CarTypeUpdatePayload payload, Integer id);

    void delete(Integer id);

    PageDto<CarTypeDto> readPage(Pageable pageable);

    List<CarTypeDto> readAll();

    CarType get(Integer id);
}
