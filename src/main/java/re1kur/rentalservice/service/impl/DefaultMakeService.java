package re1kur.rentalservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import re1kur.rentalservice.dto.make.MakeReadDto;
import re1kur.rentalservice.dto.make.MakeUpdateDto;
import re1kur.rentalservice.dto.make.MakeWriteDto;
import re1kur.rentalservice.entity.Make;
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
    public List<MakeReadDto> readAll() {
        return repo.findAll().stream().map(mapper::read).toList();
    }

    public MakeReadDto write(MakeWriteDto make) {
        Make saved = repo.save(mapper.write(make));
        return mapper.read(saved);
    }

    @Override
    public MakeReadDto read(int id) {
        return repo.findById(id).map(mapper::read).orElse(null);
    }

    @Override
    public MakeUpdateDto readUpdateById(int id) {
        return repo.findById(id).map(mapper::readUpdate).orElse(null);
    }

    @Override
    public void updateMake(MakeUpdateDto update, int id) {
        Make mapped = mapper.update(update, id);
        repo.save(mapped);
    }
}
