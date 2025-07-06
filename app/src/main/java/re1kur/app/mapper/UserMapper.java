package re1kur.app.mapper;

import org.springframework.security.core.userdetails.UserDetails;
import re1kur.app.core.dto.UserDto;
import re1kur.app.core.payload.UserPayload;
import re1kur.app.entity.user.User;

public interface UserMapper {

    UserDto read(User user);

    UserDetails security(User user);

    User write(UserPayload user);

}
