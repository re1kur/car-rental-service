package re1kur.app.service;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import re1kur.app.core.dto.CarDto;
import re1kur.app.core.dto.CarUpdateDto;
import re1kur.app.core.payload.CarUpdatePayload;
import re1kur.app.core.dto.CarFullDto;
import re1kur.app.core.dto.PageDto;
import re1kur.app.core.payload.CarPayload;
import re1kur.app.core.other.CarFilter;

public interface CarService {

    CarFullDto readFull(Integer id);

    Integer create(CarPayload car, MultipartFile title, MultipartFile[] files);

    CarUpdateDto readUpdateById(Integer id);

    void updateCar(CarUpdatePayload car, Integer id);

    PageDto<CarDto> readAll(CarFilter filter, Pageable pageable);
}
