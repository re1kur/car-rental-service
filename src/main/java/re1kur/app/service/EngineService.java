package re1kur.app.service;

import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import re1kur.app.core.dto.EngineDto;
import re1kur.app.core.dto.PageDto;
import re1kur.app.core.payload.EnginePayload;
import re1kur.app.core.payload.EngineUpdatePayload;
import re1kur.app.entity.Engine;

import java.util.List;

public interface EngineService {
    Integer create(EnginePayload payload, OidcUser user);

    EngineDto read(Integer id, OidcUser user);

    void update(EngineUpdatePayload payload, Integer id, OidcUser user);

    void delete(Integer id, OidcUser user);

    PageDto<EngineDto> readAllAsPage(Pageable pageable, OidcUser user);

    List<EngineDto> readAll();

    Engine get(Integer id);
}
