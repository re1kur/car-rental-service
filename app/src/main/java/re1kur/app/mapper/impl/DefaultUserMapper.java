package re1kur.app.mapper.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import re1kur.app.core.annotations.Mapper;
import re1kur.app.core.dto.UserDto;
import re1kur.app.core.payload.UserPayload;
import re1kur.app.entity.user.User;
import re1kur.app.entity.user.UserDetailsImpl;
import re1kur.app.mapper.UserMapper;

import java.util.List;


@Mapper
@RequiredArgsConstructor
public class DefaultUserMapper implements UserMapper {
    private final PasswordEncoder encoder;

    @Override
    public UserDto read(User user) {
        return UserDto.builder()
                .id(user.getId().toString())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .isEmailVerified(user.getEmailVerified())
                .build();
    }

    @Override
    public UserDetails security(User user) {
        List<SimpleGrantedAuthority> roles = user.getRoles().stream().map(
                role -> new SimpleGrantedAuthority(role.getName())).toList();
        return UserDetailsImpl.builder()
                .id(user.getId())
                .roles(roles)
                .password(user.getPassword())
//                .enabled(user.getEmailVerified())
                .build();
    }

    @Override
    public User write(UserPayload payload) {
        return User.builder()
                .email(payload.email())
                .fullName(payload.fullName())
                .password(encoder.encode(payload.password()))
                .build();
    }
}
