package re1kur.app.mapper;

//import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import re1kur.app.dto.user.UserReadDto;
import re1kur.app.entity.User;

public interface UserMapper {

    UserReadDto read(User user);

//    User write(OidcUserInfo token);

}
