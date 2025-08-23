package re1kur.app.service;

import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import re1kur.app.core.dto.CarTypeDto;
import re1kur.app.core.dto.PageDto;
import re1kur.app.core.payload.CarTypePayload;
import re1kur.app.core.payload.CarTypeUpdatePayload;
import re1kur.app.entity.CarType;

import java.util.List;

public interface CarTypeService {
    Integer create(CarTypePayload payload, OidcUser user);

    CarTypeDto read(Integer id, OidcUser user);

    void update(CarTypeUpdatePayload payload, Integer id, OidcUser user);

    void delete(Integer id, OidcUser user);

    PageDto<CarTypeDto> readAllAsPage(Pageable pageable, OidcUser user);

    List<CarTypeDto> readAll();

    CarType get(Integer id);
}
