package re1kur.rentalservice.mapper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import re1kur.rentalservice.annotations.Mapper;
import re1kur.rentalservice.dto.car.details.CarDetailsReadDto;
import re1kur.rentalservice.dto.car.details.CarDetailsUpdateDto;
import re1kur.rentalservice.dto.car.details.CarDetailsWriteDto;
import re1kur.rentalservice.entity.Car;
import re1kur.rentalservice.entity.CarDetails;
import re1kur.rentalservice.mapper.CarDetailsMapper;
import re1kur.rentalservice.repository.CarRepository;

@Mapper
public class DefaultCarDetailsMapper implements CarDetailsMapper {
    CarRepository carRepo;

    @Autowired
    public DefaultCarDetailsMapper(
            CarRepository carRepo
    ) {
        this.carRepo = carRepo;
    }

    @Override
    public CarDetails write(CarDetailsWriteDto details) {
        if (details.getMileage() == null &&
            details.getColor().isEmpty() &&
            details.getTransmission() == null &&
            details.getFuelType() == null)
        return null;
        else return CarDetails.builder()
                .fuelType(details.getFuelType())
                .transmission(details.getTransmission())
                .color(details.getColor())
                .mileage(details.getMileage())
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
    public CarDetailsUpdateDto readUpdate(CarDetails details) {
        if (details == null)
            return new CarDetailsUpdateDto();
        return CarDetailsUpdateDto.builder()
                .color(details.getColor())
                .fuelType(details.getFuelType())
                .mileage(details.getMileage())
                .transmission(details.getTransmission())
                .build();
    }

    @Override
    public CarDetails update(CarDetailsUpdateDto details, int id) {
        Car car = carRepo.getReferenceById(id);
        CarDetails build = CarDetails.builder()
                .mileage(details.getMileage())
                .transmission(details.getTransmission())
                .color(details.getColor())
                .fuelType(details.getFuelType())
                .car(car)
                .build();
        if (car.getDetails() != null) {
            build.setId(car.getDetails().getId());
        }
        return build;
    }
}
