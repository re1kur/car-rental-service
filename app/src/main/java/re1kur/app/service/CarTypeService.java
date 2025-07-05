package re1kur.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import re1kur.app.core.dto.CarTypeDto;
import re1kur.app.core.payload.CarTypePayload;
import re1kur.app.core.payload.CarTypeUpdatePayload;

public interface CarTypeService {
    void create(CarTypePayload payload);

    CarTypeDto get(Integer id);

    void update(CarTypeUpdatePayload payload, Integer id);

    void delete(Integer id);

    Page<CarTypeDto> getPage(Pageable pageable);
}
