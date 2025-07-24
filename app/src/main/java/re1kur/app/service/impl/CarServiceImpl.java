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
import re1kur.app.core.car.CarUpdateDto;
import re1kur.app.core.dto.PageDto;
import re1kur.app.core.payload.CarPayload;
import re1kur.app.core.other.CarFilter;
import re1kur.app.entity.car.CarInformation;
import re1kur.app.entity.image.Image;
import re1kur.app.entity.car.Car;
import re1kur.app.entity.cartype.CarType;
import re1kur.app.entity.engine.Engine;
import re1kur.app.entity.make.Make;
import re1kur.app.mapper.CarInformationMapper;
import re1kur.app.mapper.CarMapper;
import re1kur.app.repository.CarInformationRepository;
import re1kur.app.repository.CarRepository;
import re1kur.app.service.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository repo;
    private final CarMapper carMapper;
    private final FileStoreService fileService;
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
    public CarUpdateDto readUpdateById(int id) {
        return repo.findById(id).map(carMapper::readUpdate)
                .orElse(null);
    }

    @Override
    public void updateCar(CarUpdateDto car, int id) {
        Car update = carMapper.update(car, id);
        repo.save(update);
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
    public CarDto readFull(int id) {
        return repo.findById(id).map(
                carMapper::readFull).orElse(null);
    }

    @Override
    @Transactional
    public Integer create(CarPayload payload, MultipartFile titlePayload, MultipartFile[] files) {
        log.info("CREATE CAR [{}]", payload);
        Map<String, Object> result = uploadFiles(titlePayload, files);

        Image title = (Image) result.get(TITLE_IMAGE_KEY);
        List<Image> images = (List<Image>) result.get(IMAGES_KEY);

        //todo: check if exists by license plate

        Make make = makeService.get(payload.makeId());
        CarType type = carTypeService.get(payload.carTypeId());
        Engine engine = engineService.get(payload.engineId());

        Car mapped = carMapper.write(payload, make, type, engine);

        Car saved = repo.save(mapped);

        CarInformation infoMapped = infoMapper.write(payload, saved, title, images);
        infoRepo.save(infoMapped);

        log.info("CREATED CAR [{}]", saved.getId());
        return saved.getId();
    }

    private Map<String, Object> uploadFiles(MultipartFile titlePayload, MultipartFile[] filesUploads) {
        Map<String, Object> map = new HashMap<>();
        boolean titleEmptiness = titlePayload == null && titlePayload.isEmpty();
        boolean filesEmptiness = filesUploads.length == 0;

        if (!titleEmptiness) {
            Image title = fileService.uploadImage(titlePayload);
            map.put(TITLE_IMAGE_KEY, title);
        } else {
            map.put(TITLE_IMAGE_KEY, null);
        }

        if (!filesEmptiness) {
            List<Image> uploadedFiles = fileService.uploadImages(filesUploads);
            map.put(IMAGES_KEY, uploadedFiles);
        } else {
            map.put(IMAGES_KEY, List.of());
        }

        return map;
    }
}
