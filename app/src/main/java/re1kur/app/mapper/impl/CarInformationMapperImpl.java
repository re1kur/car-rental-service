package re1kur.app.mapper.impl;

import lombok.RequiredArgsConstructor;
import re1kur.app.core.annotations.Mapper;
import re1kur.app.core.dto.CarInformationDto;
import re1kur.app.core.payload.CarPayload;
import re1kur.app.core.payload.CarUpdatePayload;
import re1kur.app.entity.car.Car;
import re1kur.app.entity.car.CarInformation;
import re1kur.app.mapper.CarInformationMapper;

@Mapper
@RequiredArgsConstructor
public class CarInformationMapperImpl implements CarInformationMapper {
    @Override
    public CarInformation create(CarPayload payload, Car car) {
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

        return information;
    }

    @Override
    public CarInformationDto read(CarInformation information) {
        if (information == null)
            return null;

        return CarInformationDto.builder()
                .color(information.getColor())
                .seats(information.getSeats())
                .description(information.getDescription())
                .transmission(information.getTransmission())
                .mileage(information.getMileage())
                .fuelType(information.getFuelType())
                .build();
    }

    @Override
    public CarInformation update(CarInformation information, CarUpdatePayload payload, Car car) {
        if (information == null)
            information = CarInformation.builder().car(car).build();

        boolean hasInfo = false;

        if (isNotEmpty(payload.description())) {
            information.setDescription(payload.description());
            hasInfo = true;
        } else {
            information.setDescription(null);
        }

        if (isNotEmpty(payload.color())) {
            information.setColor(payload.color());
            hasInfo = true;
        } else {
            information.setColor(null);
        }

        if (payload.mileage() != null) {
            information.setMileage(payload.mileage());
            hasInfo = true;
        } else {
            information.setMileage(null);
        }

        if (payload.seats() != null) {
            information.setSeats(payload.seats());
            hasInfo = true;
        } else {
            information.setSeats(null);
        }

        if (isNotEmpty(payload.transmission())) {
            information.setTransmission(payload.transmission());
            hasInfo = true;
        } else {
            information.setTransmission(null);
        }

        if (isNotEmpty(payload.fuelType())) {
            information.setFuelType(payload.fuelType());
            hasInfo = true;
        } else {
            information.setFuelType(null);
        }

        if (hasInfo) {
            return information;
        } else {
            return null;
        }
    }

    private static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
}
