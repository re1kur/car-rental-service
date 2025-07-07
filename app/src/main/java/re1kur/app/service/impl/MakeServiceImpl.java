package re1kur.app.service.impl;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import re1kur.app.core.exception.MakeNotFoundException;
import re1kur.app.core.dto.MakeDto;
import re1kur.app.core.payload.MakeUpdatePayload;
import re1kur.app.core.payload.MakePayload;
import re1kur.app.entity.car.Make;
import re1kur.app.mapper.MakeMapper;
import re1kur.app.repository.MakeRepository;
import re1kur.app.service.FileStoreService;
import re1kur.app.service.MakeService;

import java.util.List;

@Service
public class MakeServiceImpl implements MakeService {
    private final MakeRepository repo;
    private final MakeMapper mapper;
    private final FileStoreService fileStoreService;

    @Autowired
    public MakeServiceImpl(
            MakeRepository repo,
            MakeMapper mapper,
            FileStoreService fileStoreService) {
        this.repo = repo;
        this.mapper = mapper;
        this.fileStoreService = fileStoreService;
    }

    @Override
    public List<MakeDto> readAll() {
        return repo.findAll().stream().map(mapper::read).toList();
    }

    @SneakyThrows
    public void write(MakePayload make) {
//        if (make.getImage() != null) {
//            String url = fileStoreService.uploadMakeImage(make.getImage());
//            make.setTitleImageUrl(url);
//        }
        repo.save(mapper.write(make));
    }

    @Override
    public MakeDto get(Integer id) {
        return repo.findById(id).map(mapper::read).orElseThrow(() -> new MakeNotFoundException("Make with ID [%d] was not found.".formatted(id)));
    }

    @SneakyThrows
    @Override
    public void update(MakeUpdatePayload update, Integer id) {
        if (update.getImage() != null) {
            String url = fileStoreService.uploadMakeImage(update.getImage());
            update.setTitleImageUrl(url);
        }
        Make mapped = mapper.update(update, id);
        repo.save(mapped);
    }
}
