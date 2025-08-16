package re1kur.app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import re1kur.app.core.dto.MakeDto;
import re1kur.app.core.exception.MakeAlreadyExistsException;
import re1kur.app.core.exception.MakeNotFoundException;
import re1kur.app.core.dto.MakeFullDto;
import re1kur.app.core.payload.MakeUpdatePayload;
import re1kur.app.core.payload.MakePayload;
import re1kur.app.entity.File;
import re1kur.app.entity.Make;
import re1kur.app.entity.MakeInformation;
import re1kur.app.mapper.MakeInformationMapper;
import re1kur.app.mapper.MakeMapper;
import re1kur.app.repository.MakeInformationRepository;
import re1kur.app.repository.MakeRepository;
import re1kur.app.service.MakeService;
import re1kur.app.service.MinioService;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MakeServiceImpl implements MakeService {
    private final MakeRepository repo;
    private final MakeMapper makeMapper;
    private final MakeInformationMapper infoMapper;
    private final MinioService fileService;
    private final MakeInformationRepository infoRepo;

    @Value("${custom.map.title_image_key}")
    private String TITLE_IMAGE_KEY;

    @Value("${custom.map.images_key}")
    private String IMAGES_KEY;

    @Override
    public List<MakeDto> readAll() {
        return repo.findAll().stream().map(makeMapper::read).toList();
    }

    @Transactional
    @Override
    public void create(MakePayload payload, MultipartFile title, MultipartFile[] files) {
        log.info("CREATE MAKE [{}]", payload);
        String name = payload.name();

        if (repo.existsByName(name))
            throw new MakeAlreadyExistsException("Make [%s] already exists.".formatted(name));

        Make mapped = makeMapper.create(payload);

        Make saved = repo.save(mapped);

        saveInformationAndImages(payload, title, files, saved);

        log.info("Make created: {}", mapped);
    }

    private void saveInformationAndImages(MakePayload payload, MultipartFile titlePayload, MultipartFile[] files, Make saved) {
        MakeInformation infoMapped = infoMapper.write(payload, saved);

        if (infoMapped != null) {
            saved.setInformation(infoMapped);
            infoRepo.save(infoMapped);
        }
        Map<String, Object> result = uploadFiles(titlePayload, files);
        File title = (File) result.get(TITLE_IMAGE_KEY);
        List<File> images = (List<File>) result.get(IMAGES_KEY);

        boolean hasImages = false;
        if (images != null && !images.isEmpty()) {
            saved.setImages(images);
            hasImages = true;
        }

        if (title != null) {
            saved.setTitleImage(title);
        }

        if (hasImages)
            repo.save(saved);
    }

    private Map<String, Object> uploadFiles(MultipartFile titlePayload, MultipartFile[] imagePayloads) {
        Map<String, Object> map = new HashMap<>();
        List<File> images = new ArrayList<>();

        File titleImage = fileService.upload(titlePayload);
        map.put(TITLE_IMAGE_KEY, titleImage);
        if (titleImage != null) images.add(titleImage);

        List<File> uploadedFiles = fileService.uploadAll(imagePayloads);
        images.addAll(uploadedFiles);

        map.put(IMAGES_KEY, images);
        return map;
    }

    @Override
    @Transactional
    public MakeFullDto read(Integer id) {
        return repo.findById(id).map(makeMapper::readFull)
                .orElseThrow(() -> new MakeNotFoundException("Make [%d] was not found.".formatted(id)));
    }

    @Override
    @Transactional
    public void update(MakeUpdatePayload payload, Integer id) {
        log.info("UPDATE MAKE [{}]", id);
        String name = payload.name();

        Make found = repo.findById(id).orElseThrow(() ->
                new MakeNotFoundException("Make [%d] was not found.".formatted(id))
        );

        if (!Objects.equals(found.getName(), name)) {
            if (repo.existsByName(name))
                throw new MakeAlreadyExistsException("Make [%s] already exists.".formatted(name));
        }

        Make updated = makeMapper.update(found, payload);

        repo.save(updated);

        log.info("UPDATED MAKE [{}]", id);
    }

    @Override
    public Make get(Integer id) {
        return repo.findById(id).orElseThrow(() ->
                new MakeNotFoundException("Make [%d] was not found.".formatted(id)));
    }
}
