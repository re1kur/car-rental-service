package re1kur.app.service;

import jakarta.validation.Valid;
//import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import re1kur.app.dto.user.UserReadDto;
import re1kur.app.dto.user.UserWriteDto;

public interface UserService
{
    UserReadDto read(String id);

//    void writeIfNotExists(OidcUserInfo userInfo);

//    void login(@Valid UserWriteDto user);
//
//    void register(@Valid UserWriteDto user);
}
