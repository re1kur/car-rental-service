package re1kur.app.service.impl;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import re1kur.app.core.make.MakeReadDto;
import re1kur.app.core.make.MakeUpdateDto;
import re1kur.app.core.make.MakeWriteDto;
import re1kur.app.entity.car.Make;
import re1kur.app.mapper.MakeMapper;
import re1kur.app.repository.MakeRepository;
import re1kur.app.service.FileStoreService;
import re1kur.app.service.MakeService;

import java.util.List;

@Service
public class DefaultMakeService implements MakeService {
    private final MakeRepository repo;
    private final MakeMapper mapper;
    private final FileStoreService fileStoreService;

    @Autowired
    public DefaultMakeService(
            MakeRepository repo,
            MakeMapper mapper,
            FileStoreService fileStoreService) {
        this.repo = repo;
        this.mapper = mapper;
        this.fileStoreService = fileStoreService;
    }

    @Override
    public List<MakeReadDto> readAll() {
        return repo.findAll().stream().map(mapper::read).toList();
    }

    @SneakyThrows
    public MakeReadDto write(MakeWriteDto make) {
        if (make.getImage() != null) {
            String url = fileStoreService.uploadMakeImage(make.getImage());
            make.setTitleImageUrl(url);
        }
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

    @SneakyThrows
    @Override
    public void updateMake(MakeUpdateDto update, int id) {
        if (update.getImage() != null) {
            String url = fileStoreService.uploadMakeImage(update.getImage());
            update.setTitleImageUrl(url);
        }
        Make mapped = mapper.update(update, id);
        repo.save(mapped);
    }
}
