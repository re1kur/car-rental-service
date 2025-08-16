package re1kur.app.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import re1kur.app.core.dto.UserDto;
import re1kur.app.core.payload.UserPayload;

public interface UserService extends UserDetailsService {
    UserDto read(String id);

    String register(UserPayload user);
}
