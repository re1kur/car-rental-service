package re1kur.rentalservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import re1kur.rentalservice.dto.car.CarReadDto;
import re1kur.rentalservice.dto.car.CarUpdateDto;
import re1kur.rentalservice.dto.car.CarWriteDto;
import re1kur.rentalservice.entity.Car;
import re1kur.rentalservice.mapper.CarDetailsMapper;
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
//    private final CarImageRepository imageRepo;
    private final CarMapper mapper;
    private final CarDetailsMapper detailsMapper;
//    private final CarImagesMapper imagesMapper;

    @Autowired
    public DefaultCarService
            (CarRepository repo,
             CarMapper mapper,
             CarDetailsRepository detailsRepo,
             CarImageRepository imageRepo,
             CarDetailsMapper detailsMapper
//             CarImagesMapper imagesMapper
             ) {
        this.repo = repo;
        this.mapper = mapper;
        this.detailsRepo = detailsRepo;
//        this.imageRepo = imageRepo;
        this.detailsMapper = detailsMapper;
//        this.imagesMapper = imagesMapper;
    }


//    @Override
//    public void writeCarDetails(CarDetailsWriteDto carDetails, int id) {
//        CarDetails details = detailsMapper.write(carDetails, id);
//        detailsRepo.save(details);
//    }

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

//    @Override
//    public List<CarReadDto> readAllWithDetails() {
//        return repo.findAll().stream().map(mapper::readWithDetails).toList();
//    }

    @Override
    public CarReadDto readByIdWithDetails(int id) {
        return repo.findById(id).map(
                mapper::readWithDetails).orElse(null);
    }

    @Override
    public Integer writeCar(CarWriteDto car) {
        Car mapped = mapper.write(car);
        Car saved = repo.save(mapped);
        return saved.getId();
    }


    @Override
    public List<CarReadDto> readAllByMake(int id) {
        return repo.findAllByMakeId(id).stream()
                .map(mapper::read).toList();
    }

//    @Override
//    public void updateCar(int id, CarUpdateDto updateDto) {
//        Optional<Car> mayBeCar = repo.findById(id);
//        mayBeCar.ifPresent(car -> {
//            car.setModel(updateDto.getModel());
//            car.setYear(updateDto.getYear());
//            car.setLicensePlate(updateDto.getLicensePlate());
//            CarDetails details = car.getDetails();
//            if (details != null) {
//                CarDetailsUpdateDto updateDetails = updateDto.getDetails();
//                details.setColor(updateDetails.getColor());
//                details.setMileage(updateDetails.getMileage());
//                details.setFuelType(updateDetails.getFuelType());
//                details.setTransmission(updateDetails.getTransmission());
//            }
//        });
//    }
}
