package re1kur.app.service;

import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.multipart.MultipartFile;
import re1kur.app.core.dto.MakeFullDto;
import re1kur.app.core.dto.MakeDto;
import re1kur.app.core.dto.PageDto;
import re1kur.app.core.payload.MakeUpdatePayload;
import re1kur.app.core.payload.MakePayload;
import re1kur.app.entity.Make;

import java.util.List;

public interface MakeService {

    PageDto<MakeDto> readAllAsPage(String name, Pageable pageable, OidcUser user);

    List<MakeDto> readAll();

    Integer create(MakePayload make, MultipartFile titleImg, MultipartFile[] files, OidcUser user);

    MakeFullDto read(Integer id, OidcUser user);

    void update(MakeUpdatePayload update, Integer id, OidcUser user);

    Make get(Integer id);
}
