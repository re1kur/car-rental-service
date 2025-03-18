package re1kur.rentalservice.mapper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import re1kur.rentalservice.annotations.Mapper;
import re1kur.rentalservice.dto.user.UserReadDto;
import re1kur.rentalservice.dto.user.UserWriteDto;
import re1kur.rentalservice.entity.User;
import re1kur.rentalservice.mapper.UserMapper;
import re1kur.rentalservice.repository.RoleRepository;

import java.util.Collections;


@Mapper
public class DefaultUserMapper implements UserMapper {
    RoleRepository roleRepo;
    BCryptPasswordEncoder encoder;

    @Autowired
    public DefaultUserMapper(
            BCryptPasswordEncoder encoder,
            RoleRepository roleRepo) {
        this.encoder = encoder;
        this.roleRepo = roleRepo;
    }

    @Override
    public UserReadDto read(User user) {
        return UserReadDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getNickname())
                .build();
    }

    @Override
    public User write(UserWriteDto user) {
        return User.builder()
                .email(user.getEmail())
                .nickname(user.getUsername())
                .password(encoder.encode(user.getPassword()))
                .roles(Collections.singletonList(roleRepo.findByName("USER")))
                .build();
    }
}
