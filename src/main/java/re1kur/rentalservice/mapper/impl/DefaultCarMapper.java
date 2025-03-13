package re1kur.rentalservice.mapper.impl;

import lombok.RequiredArgsConstructor;
import re1kur.rentalservice.annotations.Mapper;
import re1kur.rentalservice.dto.car.CarReadDto;
import re1kur.rentalservice.dto.car.CarWriteDto;
import re1kur.rentalservice.entity.Car;
import re1kur.rentalservice.mapper.CarDetailsMapper;
import re1kur.rentalservice.mapper.CarImagesMapper;
import re1kur.rentalservice.mapper.CarMapper;

import java.util.ArrayList;

@Mapper
@RequiredArgsConstructor
public class DefaultCarMapper implements CarMapper {
    CarDetailsMapper detailsMapper;
    CarImagesMapper imagesMapper;

    @Override
    public Car write(CarWriteDto writeCar) {
        return Car.builder()
                .build();
    }

    @Override
    public CarReadDto read(Car car, boolean isInformative, boolean isRender) {
        return CarReadDto.builder()
                .id(car.getId())
                .make(car.getMake().getName())
                .model(car.getModel())
                .year(car.getYear())
                .isAvailable(car.isAvailable())
                .licensePlate(car.getLicensePlate())
                .details(isInformative ? detailsMapper.read(car.getDetails()) : null)
                .images(isRender ? imagesMapper.read(car.getImages()) : new ArrayList<>())
                .build();
    }
}
