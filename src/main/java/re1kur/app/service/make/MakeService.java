package re1kur.app.service.make;

import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.multipart.MultipartFile;
import re1kur.app.dto.view.MakeFullView;
import re1kur.app.dto.view.MakeView;
import re1kur.app.dto.view.PageView;
import re1kur.app.dto.payload.MakeUpdatePayload;
import re1kur.app.dto.payload.MakePayload;
import re1kur.app.entity.Make;

import java.util.List;

public interface MakeService {

    PageView<MakeView> readAllAsPage(String name, Pageable pageable, OidcUser user);

    List<MakeView> readAll();

    Integer create(MakePayload make, MultipartFile titleImg, MultipartFile[] files, OidcUser user);

    MakeFullView read(Integer id, OidcUser user);

    void update(MakeUpdatePayload update, Integer id, OidcUser user);

    Make get(Integer id);

    void delete(Integer id, OidcUser user);
}
