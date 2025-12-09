package re1kur.app.service;

import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.multipart.MultipartFile;
import re1kur.app.model.dto.MakeFullDto;
import re1kur.app.model.dto.MakeDto;
import re1kur.app.model.dto.PageDto;
import re1kur.app.model.payload.MakeUpdatePayload;
import re1kur.app.model.payload.MakePayload;
import re1kur.app.model.entity.Make;

import java.util.List;

public interface MakeService {

    PageDto<MakeDto> readAllAsPage(String name, Pageable pageable, OidcUser user);

    List<MakeDto> readAll();

    Integer create(MakePayload make, MultipartFile titleImg, MultipartFile[] files, OidcUser user);

    MakeFullDto read(Integer id, OidcUser user);

    void update(MakeUpdatePayload update, Integer id, OidcUser user);

    Make get(Integer id);

    void delete(Integer id, OidcUser user);
}
