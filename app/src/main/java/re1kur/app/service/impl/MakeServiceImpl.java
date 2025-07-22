package re1kur.app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import re1kur.app.core.dto.MakeDto;
import re1kur.app.core.exception.MakeAlreadyExistsException;
import re1kur.app.core.exception.MakeNotFoundException;
import re1kur.app.core.dto.MakeFullDto;
import re1kur.app.core.payload.MakeUpdatePayload;
import re1kur.app.core.payload.MakePayload;
import re1kur.app.entity.image.Image;
import re1kur.app.entity.make.Make;
import re1kur.app.entity.make.MakeInformation;
import re1kur.app.mapper.MakeInformationMapper;
import re1kur.app.mapper.MakeMapper;
import re1kur.app.repository.MakeInformationRepository;
import re1kur.app.repository.MakeRepository;
import re1kur.app.service.FileStoreService;
import re1kur.app.service.MakeService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MakeServiceImpl implements MakeService {
    private final MakeRepository repo;
    private final MakeMapper makeMapper;
    private final MakeInformationMapper infoMapper;
    private final FileStoreService fileStoreService;
    private final MakeInformationRepository infoRepo;

    @Override
    public List<MakeDto> readAll() {
        return repo.findAll().stream().map(makeMapper::read).toList();
    }

    @Transactional
    @Override
    public void create(MakePayload payload, MultipartFile titleImg) {
        log.info("Received create make request: {}", payload);
        String name = payload.name();

        if (repo.existsByName(name))
            throw new MakeAlreadyExistsException("Make with name '%s' already exists.".formatted(name));

        Image image = fileStoreService.uploadImage(titleImg);

        Make mapped = makeMapper.write(payload, image);

        try {
            Make saved = repo.save(mapped);
            repo.flush();
            if (payload.hasInfo()) {
                log.info("MakePayload has info.");
                MakeInformation infoMapped = infoMapper.write(payload, saved);
                infoRepo.save(infoMapped);
            }
        } catch (Exception e) {
            fileStoreService.deleteImage(image.getId());
            throw e;
        }
        log.info("Make created: {}", mapped);
    }

    @Override
    public MakeFullDto read(Integer id) {
        return repo.findById(id).map(makeMapper::readFull).orElseThrow(() ->
                new MakeNotFoundException("Make with ID [%d] was not found.".formatted(id)));
    }

    @Override
    public void update(MakeUpdatePayload update, Integer id) {
//        if (update.getImage() != null) {
//            String url = fileStoreService.uploadMakeImage(update.getImage());
//            update.setTitleImageUrl(url);
//        }
        Make mapped = makeMapper.update(update, id);
        repo.save(mapped);
    }

    @Override
    public Make get(Integer id) {
        return repo.findById(id).orElseThrow(() ->
                new MakeNotFoundException("Make with ID [%d] was not found.".formatted(id)));
    }
}
