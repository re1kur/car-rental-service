//package re1kur.rentalservice.mapper.impl;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import re1kur.rentalservice.core.annotation.Mapper;
//import re1kur.rentalservice.core.dto.security.SecurityRole;
//import re1kur.rentalservice.core.dto.security.SecurityUser;
//import re1kur.rentalservice.entity.User;
//import re1kur.rentalservice.mapper.UserMapper;
//import re1kur.rentalservice.repository.RoleRepository;
//
//import java.util.Collection;
//import java.util.Collections;
//
//
//@Mapper
//public class DefaultUserMapper implements UserMapper {
//    RoleRepository roleRepo;
//    BCryptPasswordEncoder encoder;
//
//    @Autowired
//    public DefaultUserMapper(
//            BCryptPasswordEncoder encoder,
//            RoleRepository roleRepo) {
//        this.encoder = encoder;
//        this.roleRepo = roleRepo;
//    }
//
//    @Override
//    public UserReadDto read(User user) {
//        return UserReadDto.builder()
//                .id(user.getId())
//                .email(user.getEmail())
//                .build();
//    }
//
//    @Override
//    public User write(UserWriteDto user) {
//        return User.builder()
//                .email(user.getEmail())
//                .password(encoder.encode(user.getPassword()))
//                .roles(Collections.singletonList(roleRepo.findByName("USER")))
//                .build();
//    }
//
//    @Override
//    public SecurityUser security(User user) {
//        Collection<SecurityRole> authorities = user.getRoles().stream().map(role -> SecurityRole.builder()
//                .authority(role.getName())
//                .build()).toList();
//        return SecurityUser.builder()
//                .id(user.getId())
//                .username(user.getEmail())
//                .authorities(authorities)
//                .password(user.getPassword())
//                .build();
//    }
//}
