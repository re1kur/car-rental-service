package re1kur.app.mapper.impl;

//import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import re1kur.app.annotations.Mapper;
import re1kur.app.dto.user.UserReadDto;
import re1kur.app.entity.User;
import re1kur.app.mapper.UserMapper;



@Mapper
public class DefaultUserMapper implements UserMapper {

    @Override
    public UserReadDto read(User user) {
        return UserReadDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .isEmailVerified(user.getIsEmailVerified())
                .build();
    }

//    @Override
//    public User write(OidcUserInfo token) {
//        return User.builder()
//                .id(token.getSubject())
//                .email(token.getEmail())
//                .fullName(token.getFullName())
//                .isEmailVerified(token.getEmailVerified())
//                .build();
//    }
}
