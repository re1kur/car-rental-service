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
import re1kur.app.entity.image.Image;
import re1kur.app.entity.make.Make;
import re1kur.app.entity.make.MakeInformation;
import re1kur.app.mapper.MakeInformationMapper;
import re1kur.app.mapper.MakeMapper;
import re1kur.app.repository.MakeInformationRepository;
import re1kur.app.repository.MakeRepository;
import re1kur.app.service.FileStoreService;
import re1kur.app.service.MakeService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MakeServiceImpl implements MakeService {
    private final MakeRepository repo;
    private final MakeMapper makeMapper;
    private final MakeInformationMapper infoMapper;
    private final FileStoreService fileService;
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

        Make mapped = makeMapper.write(payload);

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
        Image title = (Image) result.get(TITLE_IMAGE_KEY);
        List<Image> images = (List<Image>) result.get(IMAGES_KEY);

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

    private Map<String, Object> uploadFiles(MultipartFile titlePayload, MultipartFile[] filesUploads) {
        Map<String, Object> map = new HashMap<>();
        List<Image> images = new ArrayList<>();

        if (titlePayload != null && !titlePayload.isEmpty()) {
            Image titleImage = fileService.uploadImage(titlePayload);
            map.put(TITLE_IMAGE_KEY, titleImage);
            images.add(titleImage);
        } else {
            map.put(TITLE_IMAGE_KEY, null);
        }

        if (filesUploads != null && filesUploads.length > 0) {
            List<Image> uploadedFiles = fileService.uploadImages(filesUploads);
            images.addAll(uploadedFiles);
        }

        map.put(IMAGES_KEY, images);
        return map;
    }

    @Override
    @Transactional
    public MakeFullDto read(Integer id) {
        return repo.findById(id).map(makeMapper::readFull)
                .orElseThrow(() -> new MakeNotFoundException("Make with ID [%d] was not found.".formatted(id)));
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
