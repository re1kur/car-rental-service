package re1kur.rentalservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import re1kur.rentalservice.dto.security.SecurityUser;
import re1kur.rentalservice.dto.user.UserReadDto;
import re1kur.rentalservice.dto.user.UserWriteDto;
import re1kur.rentalservice.entity.User;
import re1kur.rentalservice.mapper.UserMapper;
import re1kur.rentalservice.repository.UserRepository;
import re1kur.rentalservice.service.UserService;

@Service
public class DefaultUserService implements UserService {
    private final UserRepository repo;
    private final UserMapper mapper;

    @Autowired
    public DefaultUserService(UserRepository repo, UserMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public SecurityUser loadUserByUsername(String email) throws UsernameNotFoundException {
        return repo.findByEmail(email).map(mapper::security)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    @Override
    public int write(UserWriteDto user) {
        User write = mapper.write(user);
        return repo.save(write).getId();
    }

    @Override
    public UserReadDto read(int id) {
        return repo.findById(id).map(mapper::read).orElse(null);
    }
}
