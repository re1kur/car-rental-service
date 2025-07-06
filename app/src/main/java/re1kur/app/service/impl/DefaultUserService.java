package re1kur.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re1kur.app.core.dto.UserDto;
import re1kur.app.core.exception.UserEmailAlreadyRegisteredException;
import re1kur.app.core.exception.UserNotFoundException;
import re1kur.app.core.payload.UserPayload;
import re1kur.app.entity.user.User;
import re1kur.app.mapper.UserMapper;
import re1kur.app.repository.UserRepository;
import re1kur.app.service.UserService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {
    private final UserRepository repo;
    private final UserMapper mapper;

    @Override
    public UserDto read(String id) {
        return repo.findById(UUID.fromString(id))
                .map(mapper::read)
                .orElseThrow(() -> new UserNotFoundException(""));
    }

    @Override
    @Transactional
    public String register(UserPayload user) {
        if (repo.existsByEmail(user.email())) throw new UserEmailAlreadyRegisteredException("");
        User mapped = mapper.write(user);
        User saved = repo.save(mapped);
        return saved.getId().toString();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repo.findByEmail(email).orElseThrow(() -> new UserNotFoundException(""));
        return mapper.security(user);
    }
}

