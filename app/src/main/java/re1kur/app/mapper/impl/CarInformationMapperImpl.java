package re1kur.app.mapper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import re1kur.app.core.annotations.Mapper;
import re1kur.app.core.car.details.CarDetailsReadDto;
import re1kur.app.core.car.details.CarDetailsUpdateDto;
import re1kur.app.core.car.details.CarDetailsWriteDto;
import re1kur.app.core.payload.CarPayload;
import re1kur.app.entity.car.Car;
import re1kur.app.entity.car.CarInformation;
import re1kur.app.entity.image.Image;
import re1kur.app.mapper.CarInformationMapper;
import re1kur.app.repository.CarRepository;

import java.util.List;

@Mapper
public class CarInformationMapperImpl implements CarInformationMapper {
    CarRepository carRepo;

    @Autowired
    public CarInformationMapperImpl(
            CarRepository carRepo
    ) {
        this.carRepo = carRepo;
    }

    @Override
    public CarInformation write(CarDetailsWriteDto details) {
        if (details.getMileage() == null &&
            details.getColor().isEmpty() &&
            details.getTransmission() == null &&
            details.getFuelType() == null)
        return null;
        else return CarInformation.builder()
                .fuelType(details.getFuelType())
                .transmission(details.getTransmission())
                .color(details.getColor())
                .mileage(details.getMileage())
                .build();
    }

    @Override
    public CarDetailsReadDto read(CarInformation details) {
        if (details == null)
            return null;
        return CarDetailsReadDto.builder()
                .color(details.getColor())
                .fuelType(details.getFuelType())
                .mileage(details.getMileage())
                .transmission(details.getTransmission())
                .build();
    }

    @Override
    public CarDetailsUpdateDto readUpdate(CarInformation details) {
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
    public CarInformation update(CarDetailsUpdateDto details, int id) {
        if (details.getMileage() == null &&
            details.getColor().isEmpty() &&
            details.getTransmission() == null &&
            details.getFuelType() == null)
            return null;
        Car car = carRepo.getReferenceById(id);
        CarInformation build = CarInformation.builder()
                .mileage(details.getMileage())
                .transmission(details.getTransmission())
                .color(details.getColor())
                .fuelType(details.getFuelType())
                .car(car)
                .build();
//        if (car.getDetails() != null) {
//            build.setId(car.getDetails().getId());
//        }
        return build;
    }

    @Override
    public CarInformation write(CarPayload payload, Car car, Image title, List<Image> images) {
        CarInformation information = CarInformation.builder().car(car).build();

        boolean hasInfo = false;

        if (isNotEmpty(payload.description())) {
            information.setDescription(payload.description());
            hasInfo = true;
        }
        if (isNotEmpty(payload.color())) {
            information.setColor(payload.color());
            hasInfo = true;
        }
        if (payload.mileage() != null) {
            information.setMileage(payload.mileage());
            hasInfo = true;
        }
        if (payload.seats() != null) {
            information.setSeats(payload.seats());
            hasInfo = true;
        }
        if (isNotEmpty(payload.transmission())) {
            information.setTransmission(payload.transmission());
            hasInfo = true;
        }
        if (isNotEmpty(payload.fuelType())) {
            information.setFuelType(payload.fuelType());
            hasInfo = true;
        }

        if (hasInfo) {
            car.setInformation(information);
        }

        if (title != null) car.setTitleImage(title);
        if (images != null && !images.isEmpty()) car.setImages(images);

        return information;
    }

    private static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
}
