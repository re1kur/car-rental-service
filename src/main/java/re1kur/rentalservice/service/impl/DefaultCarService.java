package re1kur.rentalservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import re1kur.rentalservice.dto.car.CarReadDto;
import re1kur.rentalservice.dto.car.CarWriteDto;
import re1kur.rentalservice.dto.car.details.CarDetailsWriteDto;
import re1kur.rentalservice.dto.car.images.CarImageWriteDto;
import re1kur.rentalservice.entity.Car;
import re1kur.rentalservice.mapper.CarDetailsMapper;
import re1kur.rentalservice.mapper.CarImagesMapper;
import re1kur.rentalservice.mapper.CarMapper;
import re1kur.rentalservice.repository.CarDetailsRepository;
import re1kur.rentalservice.repository.CarImageRepository;
import re1kur.rentalservice.repository.CarRepository;
import re1kur.rentalservice.service.CarService;

import java.util.List;

@Service
public class DefaultCarService implements CarService {
    private final CarRepository repo;
    private final CarDetailsRepository detailsRepo;
    private final CarImageRepository imageRepo;
    private final CarMapper mapper;
    private final CarDetailsMapper detailsMapper;
    private final CarImagesMapper imagesMapper;

    @Autowired
    public DefaultCarService
            (CarRepository repo,
             CarMapper mapper,
             CarDetailsRepository detailsRepo,
             CarImageRepository imageRepo,
             CarDetailsMapper detailsMapper,
             CarImagesMapper imagesMapper
             ) {
        this.repo = repo;
        this.mapper = mapper;
        this.detailsRepo = detailsRepo;
        this.imageRepo = imageRepo;
        this.detailsMapper = detailsMapper;
        this.imagesMapper = imagesMapper;
    }


    @Override
    public List<CarReadDto> findAll(boolean isInformative, boolean isRender) {
        return repo.findAll().stream().map(car -> mapper.read(car, isInformative, isRender)).toList();
    }

    @Override
    public CarReadDto findById(int id, boolean isInformative, boolean isRender) {
        return repo.findById(id).map(
                        car -> mapper.read(car, isInformative, isRender))
                .orElse(null);
    }

    @Override
    public CarReadDto writeCar(CarWriteDto newCar) {
        Car mapped = mapper.write(newCar);
        Car saved = repo.save(mapped);

        if (newCar.getDetails() != null) {
            CarDetailsWriteDto newDetails = newCar.getDetails();
            newDetails.setCar(saved);
            detailsRepo.save(detailsMapper.write(newDetails));

        }
        if (newCar.getImage() != null) {
            CarImageWriteDto newImage = newCar.getImage();
            newImage.setCar(saved);
            imageRepo.save(imagesMapper.writeImage(newImage));
        }
        return mapper.read(saved, true, true);
    }
}
