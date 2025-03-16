package re1kur.rentalservice.mapper.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import re1kur.rentalservice.annotations.Mapper;
import re1kur.rentalservice.dto.car.CarReadDto;
import re1kur.rentalservice.dto.car.CarUpdateDto;
import re1kur.rentalservice.dto.car.CarWriteDto;
import re1kur.rentalservice.entity.Car;
import re1kur.rentalservice.mapper.CarDetailsMapper;
import re1kur.rentalservice.mapper.CarImagesMapper;
import re1kur.rentalservice.mapper.CarMapper;
import re1kur.rentalservice.mapper.MakeMapper;
import re1kur.rentalservice.repository.MakeRepository;

import java.util.ArrayList;

@Mapper
public class DefaultCarMapper implements CarMapper {
    MakeRepository makeRepo;
    CarDetailsMapper detailsMapper;
    CarImagesMapper imagesMapper;
    MakeMapper makeMapper;

    @Autowired
    public DefaultCarMapper(
            CarDetailsMapper detailsMapper,
            CarImagesMapper imagesMapper,
            MakeRepository makeRepo,
            MakeMapper makeMapper
    ) {
        this.detailsMapper = detailsMapper;
        this.imagesMapper = imagesMapper;
        this.makeRepo = makeRepo;
        this.makeMapper = makeMapper;
    }

    @Override
    public Car write(CarWriteDto writeCar) {
        return Car.builder()
                .make(makeRepo.getReferenceById(writeCar.getMakeId()))
                .model(writeCar.getModel())
                .year(writeCar.getYear())
                .licensePlate(writeCar.getLicensePlate())
                .build();
    }

    @Override
    public CarReadDto read(Car car, boolean isInformative, boolean isRender) {
        return CarReadDto.builder()
                .id(car.getId())
                .make(makeMapper.read(car.getMake()))
                .model(car.getModel())
                .year(car.getYear())
                .isAvailable(car.isAvailable())
                .licensePlate(car.getLicensePlate())
                .details(isInformative ? detailsMapper.read(car.getDetails()) : null)
                .images(isRender ? imagesMapper.read(car.getImages()) : new ArrayList<>())
                .build();
    }

    @Override
    public Car update(CarUpdateDto car) {
        return Car.builder()
                .id(car.getId())
                .make(makeRepo.getReferenceById(car.getMakeId()))
                .model(car.getModel())
                .year(car.getYear())
                .licensePlate(car.getLicensePlate())
                .build();
    }
}
