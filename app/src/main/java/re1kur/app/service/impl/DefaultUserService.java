package re1kur.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import re1kur.app.dto.user.UserReadDto;
import re1kur.app.dto.user.UserWriteDto;
import re1kur.app.entity.User;
import re1kur.app.mapper.UserMapper;
import re1kur.app.repository.UserRepository;
import re1kur.app.service.UserService;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {
    private final UserRepository repo;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;

    @Override
    public UserReadDto read(String id) {
        return repo.findById(id).map(mapper::read).orElse(null);
    }

//    @Override
//    @Transactional
//    public void writeIfNotExists(OidcUserInfo userInfo) {
//        if (!repo.existsById(userInfo.getSubject()))
//            repo.save(mapper.write(userInfo));
//    }

//    @Override
//    public void login(UserWriteDto payload) {
//        User user = repo.findByEmail(payload.getEmail()).orElseThrow();
//        if (encoder.matches(payload.getPassword(), user.getPassword())) {
//
//        }
//    }
//
//    @Override
//    public void register(UserWriteDto user) {
//
//    }
}

