package re1kur.rentalservice.mapper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import re1kur.rentalservice.annotations.Mapper;
import re1kur.rentalservice.dto.car.details.CarDetailsReadDto;
import re1kur.rentalservice.dto.car.details.CarDetailsUpdateDto;
import re1kur.rentalservice.dto.car.details.CarDetailsWriteDto;
import re1kur.rentalservice.entity.CarDetails;
import re1kur.rentalservice.mapper.CarDetailsMapper;
import re1kur.rentalservice.repository.CarRepository;

@Mapper
public class DefaultCarDetailsMapper implements CarDetailsMapper {
    CarRepository carRepo;

    @Autowired
    public DefaultCarDetailsMapper(CarRepository carRepo) {
        this.carRepo = carRepo;
    }

    @Override
    public CarDetails write(CarDetailsWriteDto writeCarDetails) {
        return CarDetails.builder()
                .car(writeCarDetails.getCar())
                .mileage(writeCarDetails.getMileage())
                .color(writeCarDetails.getColor())
                .transmission(writeCarDetails.getTransmission())
                .fuelType(writeCarDetails.getFuelType())
                .build();
    }

    @Override
    public CarDetailsReadDto read(CarDetails details) {
        if (details == null)
            return null;
        return CarDetailsReadDto.builder()
                .id(details.getId())
                .color(details.getColor())
                .fuelType(details.getFuelType())
                .mileage(details.getMileage())
                .transmission(details.getTransmission())
                .build();
    }

    @Override
    public CarDetails update(CarDetailsUpdateDto details) {
        return CarDetails.builder()
                .id(details.getId())
                .car(carRepo.getReferenceById(details.getCar().getId()))
                .color(details.getColor())
                .fuelType(details.getFuelType())
                .mileage(details.getMileage())
                .transmission(details.getTransmission())
                .build();
    }
}
