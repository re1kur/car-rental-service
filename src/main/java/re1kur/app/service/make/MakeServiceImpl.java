package re1kur.app.service.make;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import re1kur.app.dto.view.MakeView;
import re1kur.app.dto.view.PageView;
import re1kur.app.exception.MakeAlreadyExistsException;
import re1kur.app.exception.MakeNotFoundException;
import re1kur.app.dto.view.MakeFullView;
import re1kur.app.dto.payload.MakeUpdatePayload;
import re1kur.app.dto.payload.MakePayload;
import re1kur.app.entity.File;
import re1kur.app.entity.Make;
import re1kur.app.entity.MakeInformation;
import re1kur.app.mapper.make.MakeInformationMapper;
import re1kur.app.mapper.make.MakeMapper;
import re1kur.app.repository.make.MakeInformationRepository;
import re1kur.app.repository.make.MakeRepository;
import re1kur.app.service.minio.MinioService;

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
    public PageView<MakeView> readAllAsPage(String name, Pageable pageable, OidcUser user) {
        String logUser = user == null ? "Anonymous" : user.getSubject();
        log.info("READ MAKES PAGE REQUEST BY USER [{}]", logUser);

        return makeMapper.readPage(repo.findAll(pageable, name));
    }

    @Override
    public List<MakeView> readAll() {
        return repo.findAll().stream().map(makeMapper::read).toList();
    }

    @Override
    @Transactional
    public Integer create(MakePayload payload, MultipartFile title, MultipartFile[] files, OidcUser user) {
        String logUser = user == null ? "Anonymous" : user.getSubject();
        log.info("CREATE MAKE [{}] REQUEST BY USER [{}]", payload, logUser);
        String name = payload.name();

        if (repo.existsByName(name))
            throw new MakeAlreadyExistsException("Make [%s] already exists.".formatted(name));

        Make mapped = makeMapper.create(payload);

        Make saved = repo.save(mapped);

        saveInformationAndImages(payload, title, files, saved);

        Integer makeId = saved.getId();
        log.info("CREATED MAKE [{}] REQUEST BY USER [{}]", makeId, logUser);
        return makeId;
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
    public MakeFullView read(Integer id, OidcUser user) {
        String logUser = user == null ? "Anonymous" : user.getSubject();
        log.info("READ MAKE FULL [{}] REQUEST BY USER [{}]", id, logUser);

        return repo.findById(id).map(makeMapper::readFull)
                .orElseThrow(() -> new MakeNotFoundException("Make [%d] was not found.".formatted(id)));
    }

    @Override
    @Transactional
    public void update(MakeUpdatePayload payload, Integer id, OidcUser user) {
        String logUser = user == null ? "Anonymous" : user.getSubject();
        log.info("UPDATE MAKE [{}] REQUEST BY USER [{}]", id, logUser);
        String name = payload.name();

        Make found = repo.findById(id).orElseThrow(() ->
                new MakeNotFoundException("Make [%d] was not found.".formatted(id))
        );

        checkConflicts(found, name);

        Make updated = makeMapper.update(found, payload);

        repo.save(updated);

        log.info("UPDATED MAKE [{}] REQUEST BY USER [{}]", id, logUser);
    }

    private void checkConflicts(Make found, String name) {
        if (!Objects.equals(found.getName(), name)) {
            if (repo.existsByName(name))
                throw new MakeAlreadyExistsException("Make [%s] already exists.".formatted(name));
        }
    }

    @Override
    public Make get(Integer id) {
        return repo.findById(id).orElseThrow(() ->
                new MakeNotFoundException("Make [%d] was not found.".formatted(id)));
    }

    @Override
    @Transactional
    public void delete(Integer id, OidcUser user) {
        String logUser = user == null ? "Anonymous" : user.getSubject();
        log.info("DELETE MAKE [{}] REQUEST BY USER [{}]", id, logUser);

        if (!repo.existsById(id))
            throw new MakeNotFoundException("Make [%s] was not found.".formatted(id));

        repo.deleteById(id);

        log.info("DELETED MAKE [{}] REQUEST BY USER [{}]", id, logUser);
    }
}
