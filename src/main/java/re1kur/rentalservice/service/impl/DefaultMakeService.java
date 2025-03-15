package re1kur.rentalservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import re1kur.rentalservice.dto.make.MakeReadDto;
import re1kur.rentalservice.mapper.MakeMapper;
import re1kur.rentalservice.repository.MakeRepository;
import re1kur.rentalservice.service.MakeService;

import java.util.List;

@Service
public class DefaultMakeService implements MakeService {
    private final MakeRepository repo;
    private final MakeMapper mapper;

    @Autowired
    public DefaultMakeService(
            MakeRepository repo,
            MakeMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public List<MakeReadDto> findAll() {
        return repo.findAll().stream().map(mapper::read).toList();
    }
}
