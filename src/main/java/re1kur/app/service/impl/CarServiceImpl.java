package re1kur.app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import re1kur.app.core.dto.CarDto;
import re1kur.app.core.dto.CarUpdateDto;
import re1kur.app.core.exception.CarAlreadyExistsException;
import re1kur.app.core.exception.CarNotFoundException;
import re1kur.app.core.payload.CarUpdatePayload;
import re1kur.app.core.dto.CarFullDto;
import re1kur.app.core.dto.PageDto;
import re1kur.app.core.payload.CarPayload;
import re1kur.app.core.other.CarFilter;
import re1kur.app.entity.*;
import re1kur.app.mapper.CarInformationMapper;
import re1kur.app.mapper.CarMapper;
import re1kur.app.repository.CarInformationRepository;
import re1kur.app.repository.CarRepository;
import re1kur.app.service.*;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository repo;
    private final CarMapper carMapper;
    private final MinioService fileService;
    private final MakeService makeService;
    private final CarInformationMapper infoMapper;
    private final CarInformationRepository infoRepo;


    @Override
    @Transactional
    public Integer create(CarPayload payload, MultipartFile titlePayload, MultipartFile[] files, OidcUser user) {
        String logUser = user == null ? "Anonymous" : user.getSubject();
        log.info("CREATE CAR [{}] REQUEST BY USER [{}]", payload, logUser);
        String licensePlate = payload.licensePlate();

        if (repo.existsByLicensePlate(licensePlate))
            throw new CarAlreadyExistsException("Car [%s] already exists.".formatted(licensePlate));

        Make make = makeService.get(payload.makeId());

        Car mapped = carMapper.write(payload, make);

        Car saved = repo.save(mapped);

        saveImagesAndInformation(payload, titlePayload, files, saved);

        log.info("CREATED CAR [{}] BY USER [{}]", saved.getId(), logUser);
        return saved.getId();
    }

    private void saveImagesAndInformation(CarPayload payload, MultipartFile titlePayload, MultipartFile[] files, Car car) {
        CarInformation infoMapped = infoMapper.create(payload, car);
        if (infoMapped != null) {
            car.setInformation(infoMapped);
            infoRepo.save(infoMapped);
        }

        Map<String, Object> result = uploadFiles(titlePayload, files);
        File title = (File) result.get("title");
        List<File> images = (List<File>) result.get("images");

        boolean hasImages = false;
        if (images != null && !images.isEmpty()) {
            car.setImages(images);
            hasImages = true;
        }

        if (title != null) {
            car.setTitleImage(title);
        }

        if (hasImages)
            repo.save(car);
    }

    @Override
    @Transactional
    public CarUpdateDto readUpdateById(Integer id) {
        return repo.findById(id)
                .map(carMapper::readUpdate)
                .orElseThrow(() -> new CarNotFoundException("Car [%s] was not found.".formatted(id)));
    }

    @Override
    public PageDto<CarDto> readAll(CarFilter filter, Pageable pageable, OidcUser user) {
        log.info("READ ALL BY FILTER [{}] BY USER [{}]", filter, user == null ? "Anonymous" : user.getSubject());

        Page<Car> found = repo.findAll(
                filter.getModel(), filter.getMakeId(), filter.getYear(),
                filter.getCarType(), filter.getEngine(), pageable);

        return carMapper.readPage(found);
    }

    @Override
    public Car getById(Integer carId) {
        return repo.findById(carId)
                .orElseThrow(() -> new CarNotFoundException("Car [%d] was not found.".formatted(carId)));
    }

    @Override
    @Transactional
    public void delete(Integer id, OidcUser user) {
        String logUser = user == null ? "Anonymous" : user.getSubject();
        log.info("DELETE CAR [{}] REQUEST BY USER [{}]", id, logUser);

        if (!repo.existsById(id))
            throw new CarNotFoundException("Car [%s] was not found.".formatted(id));

        repo.deleteById(id);

        log.info("DELETED CAR [{}] REQUEST BY USER [{}]", id, logUser);
    }

    @Override
    @Transactional
    public CarFullDto readFull(Integer id, OidcUser user) {
        log.info("READ CAR FULL [{}] REQUEST BY [{}]", id, user == null ? "Anonymous" : user.getSubject());
        return repo.findById(id).map(
                carMapper::readFull).orElseThrow(() -> new CarNotFoundException("Car [%s] was not found.".formatted(id)));
    }

    @Override
    @Transactional
    public void updateCar(CarUpdatePayload payload, Integer id, OidcUser user) {
        String logUser = user == null ? "Anonymous" : user.getSubject();
        log.info("UPDATE CAR [{}] REQUEST BY USER [{}]", id, logUser);

        Car found = repo.findById(id)
                .orElseThrow(() -> new CarNotFoundException("Car [%d] was not found.".formatted(id)));

        String licensePlate = payload.licensePlate();
        checkConflicts(licensePlate, found);

        Make make = makeService.get(payload.makeId());

        Car updated = carMapper.update(found, payload, make);

        repo.save(updated);

        log.info("UPDATED CAR [{}] BY USER [{}]", id, logUser);
    }

    private void checkConflicts(String licensePlate, Car found) {
        if (!Objects.equals(licensePlate, found.getLicensePlate())) {
            if (repo.existsByLicensePlate(licensePlate))
                throw new CarAlreadyExistsException("Car [%s] already exists.".formatted(licensePlate));
        }
    }

    private Map<String, Object> uploadFiles(MultipartFile titlePayload, MultipartFile[] imagePayloads) {
        Map<String, Object> map = new HashMap<>();
        List<File> images = new ArrayList<>();

        File titleImage = fileService.upload(titlePayload);
        map.put("title", titleImage);
        if (titleImage != null) images.add(titleImage);

        List<File> uploadedFiles = fileService.uploadAll(imagePayloads);
        images.addAll(uploadedFiles);

        map.put("images", images);
        return map;
    }
}
