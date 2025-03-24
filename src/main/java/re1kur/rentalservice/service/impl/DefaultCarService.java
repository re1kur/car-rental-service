package re1kur.rentalservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import re1kur.rentalservice.dto.car.CarReadDto;
import re1kur.rentalservice.dto.car.CarUpdateDto;
import re1kur.rentalservice.dto.car.CarWriteDto;
import re1kur.rentalservice.dto.car.images.CarImageWriteDto;
import re1kur.rentalservice.entity.Car;
import re1kur.rentalservice.mapper.CarMapper;
import re1kur.rentalservice.repository.CarRepository;
import re1kur.rentalservice.service.CarService;
import re1kur.rentalservice.service.FileStoreService;

import java.io.IOException;
import java.util.List;

@Service
public class DefaultCarService implements CarService {
    private final CarRepository repo;
    private final CarMapper mapper;
    private final FileStoreService fileService;

    @Autowired
    public DefaultCarService
            (CarRepository repo,
             CarMapper mapper,
             FileStoreService fileService) {
        this.repo = repo;
        this.mapper = mapper;
        this.fileService = fileService;
    }

    @Override
    public CarUpdateDto readUpdateById(int id) {
        return repo.findById(id).map(mapper::readUpdate)
                .orElse(null);
    }

    @Override
    public void updateCar(CarUpdateDto car, int id) {
        Car update = mapper.update(car, id);
        repo.save(update);
    }

    @Override
    public List<CarReadDto> readAll() {
        return repo.findAll().stream().map(mapper::read).toList();
    }

    @Override
    public CarReadDto readByIdWithDetails(int id) {
        return repo.findById(id).map(
                mapper::readWithDetails).orElse(null);
    }

    @Override
    public Integer writeCar(CarWriteDto car) throws IOException {
        if (!car.getImage().getImage().getOriginalFilename().equals("")) {
            CarImageWriteDto upload = fileService.upload(car.getImage());
            car.setImage(upload);
        } else {
            car.setImage(null);
        }
        Car mapped = mapper.write(car);
        Car saved = repo.save(mapped);
        return saved.getId();
    }


    @Override
    public List<CarReadDto> readAllByMake(int id) {
        return repo.findAllByMakeId(id).stream()
                .map(mapper::read).toList();
    }

}
