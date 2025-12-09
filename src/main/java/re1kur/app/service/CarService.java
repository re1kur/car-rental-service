package re1kur.app.service;

import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.multipart.MultipartFile;
import re1kur.app.model.dto.CarDto;
import re1kur.app.model.dto.CarUpdateDto;
import re1kur.app.model.payload.CarUpdatePayload;
import re1kur.app.model.dto.CarFullDto;
import re1kur.app.model.dto.PageDto;
import re1kur.app.model.payload.CarPayload;
import re1kur.app.core.filter.CarFilter;
import re1kur.app.model.entity.Car;

public interface CarService {

    CarFullDto readFull(Integer id, OidcUser user);

    Integer create(CarPayload car, MultipartFile title, MultipartFile[] files, OidcUser user);

    CarUpdateDto readUpdateById(Integer id);

    void updateCar(CarUpdatePayload car, Integer id, OidcUser subject);

    PageDto<CarDto> readAll(CarFilter filter, Pageable pageable, OidcUser user);

    Car getById(Integer carId);

    void delete(Integer id, OidcUser user);
}
