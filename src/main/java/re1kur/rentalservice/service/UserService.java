package re1kur.rentalservice.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import re1kur.rentalservice.dto.user.UserReadDto;
import re1kur.rentalservice.dto.user.UserWriteDto;

public interface UserService extends UserDetailsService {
    int write(UserWriteDto user);

    UserReadDto read(int id);
}
