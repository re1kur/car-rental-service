package re1kur.rentalservice.mapper;

import re1kur.rentalservice.dto.security.SecurityUser;
import re1kur.rentalservice.dto.user.UserReadDto;
import re1kur.rentalservice.dto.user.UserWriteDto;
import re1kur.rentalservice.entity.User;

public interface UserMapper {

    UserReadDto read(User user);

    User write(UserWriteDto user);

    SecurityUser security(User user);
}
