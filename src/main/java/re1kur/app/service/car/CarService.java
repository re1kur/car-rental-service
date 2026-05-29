package re1kur.app.service.car;

import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.multipart.MultipartFile;
import re1kur.app.dto.view.CarView;
import re1kur.app.dto.view.CarUpdateView;
import re1kur.app.dto.payload.CarUpdatePayload;
import re1kur.app.dto.view.CarFullView;
import re1kur.app.dto.view.PageView;
import re1kur.app.dto.payload.CarPayload;
import re1kur.app.dto.filter.CarFilter;
import re1kur.app.entity.Car;

public interface CarService {

    CarFullView readFull(Integer id, OidcUser user);

    Integer create(CarPayload car, MultipartFile title, MultipartFile[] files, OidcUser user);

    CarUpdateView readUpdateById(Integer id);

    void updateCar(CarUpdatePayload car, MultipartFile title, MultipartFile[] files, Integer id, OidcUser subject);

    CarFullView setAvailability(Integer id, boolean available, OidcUser user);

    PageView<CarView> readAll(CarFilter filter, Pageable pageable, OidcUser user);

    Car getById(Integer carId);

    void delete(Integer id, OidcUser user);
}
