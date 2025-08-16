package re1kur.app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final CarTypeService carTypeService;
    private final MakeService makeService;
    private final EngineService engineService;
    private final CarInformationMapper infoMapper;
    private final CarInformationRepository infoRepo;


    @Value("${custom.map.title_image_key}")
    private String TITLE_IMAGE_KEY;

    @Value("${custom.map.images_key}")
    private String IMAGES_KEY;


    @Override
    @Transactional
    public Integer create(CarPayload payload, MultipartFile titlePayload, MultipartFile[] files) {
        log.info("CREATE CAR [{}]", payload);
        String licensePlate = payload.licensePlate();

        if (repo.existsByLicensePlate(licensePlate))
            throw new CarAlreadyExistsException("Car [%s] already exists.".formatted(licensePlate));

        Make make = makeService.get(payload.makeId());
        CarType type = carTypeService.get(payload.carTypeId());
        Engine engine = engineService.get(payload.engineId());

        Car mapped = carMapper.write(payload, make, type, engine);

        Car saved = repo.save(mapped);

        saveImagesAndInformation(payload, titlePayload, files, saved);

        log.info("CREATED CAR [{}]", saved.getId());
        return saved.getId();
    }

    private void saveImagesAndInformation(CarPayload payload, MultipartFile titlePayload, MultipartFile[] files, Car car) {
        CarInformation infoMapped = infoMapper.create(payload, car);
        if (infoMapped != null) {
            car.setInformation(infoMapped);
            infoRepo.save(infoMapped);
        }

        Map<String, Object> result = uploadFiles(titlePayload, files);
        File title = (File) result.get(TITLE_IMAGE_KEY);
        List<File> images = (List<File>) result.get(IMAGES_KEY);

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
                .orElse(null);
    }

    @Override
    public PageDto<CarDto> readAll(CarFilter filter, Pageable pageable) {
        String model = filter.getModel();
        Integer makeId = filter.getMakeId();
        Integer year = filter.getYear();

        Page<Car> found = repo.findAll(model, makeId, year, pageable);

        return carMapper.readPage(found);
    }

    @Override
    @Transactional
    public CarFullDto readFull(Integer id) {
        return repo.findById(id).map(
                carMapper::readFull).orElse(null);
    }

    @Override
    @Transactional
    public void updateCar(CarUpdatePayload payload, Integer id) {
        log.info("UPDATE CAR [{}] REQUEST", id);

        Car found = repo.findById(id)
                .orElseThrow(() -> new CarNotFoundException("Car [%d] was not found.".formatted(id)));

        String licensePlate = payload.licensePlate();
        if (!Objects.equals(licensePlate, found.getLicensePlate())) {
            if (repo.existsByLicensePlate(licensePlate))
                throw new CarAlreadyExistsException("Car [%s] already exists.".formatted(licensePlate));
        }

        Make make = makeService.get(payload.makeId());
        CarType type = carTypeService.get(payload.carTypeId());
        Engine engine = engineService.get(payload.engineId());

        Car updated = carMapper.update(found, payload, make, type, engine);

        repo.save(updated);

        log.info("UPDATED CAR [{}]", id);
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

}
