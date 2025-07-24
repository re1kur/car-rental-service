package re1kur.app.mapper.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import re1kur.app.core.annotations.Mapper;
import re1kur.app.core.dto.CarDto;
import re1kur.app.core.car.CarUpdateDto;
import re1kur.app.core.dto.*;
import re1kur.app.core.payload.CarPayload;
import re1kur.app.entity.image.Image;
import re1kur.app.entity.car.*;
import re1kur.app.entity.cartype.CarType;
import re1kur.app.entity.engine.Engine;
import re1kur.app.entity.make.Make;
import re1kur.app.mapper.*;

import java.util.Collection;
import java.util.List;


@Mapper
@RequiredArgsConstructor
public class CarMapperImpl implements CarMapper {
    private final MakeMapper makeMapper;
    private final CarTypeMapper carTypeMapper;
    private final EngineMapper engineMapper;
    private final ImageMapper imageMapper;

    @Override
    public Car write(CarPayload payload, Make make, CarType type, Engine engine) {
        return Car.builder()
                .make(make)
                .carType(type)
                .engine(engine)
                .model(payload.model())
                .year(payload.year())
                .licensePlate(payload.licensePlate())
                .build();


    }

    @Override
    public CarDto read(Car car) {
        Image titleImage = car.getTitleImage();
        return CarDto.builder()
                .id(car.getId())
                .model(car.getModel())
                .year(car.getYear())
                .licensePlate(car.getLicensePlate())
                .make(makeMapper.readShort(car.getMake()))
                .carType(carTypeMapper.read(car.getCarType()))
                .engine(engineMapper.read(car.getEngine()))
                .titleImage(titleImage != null ? imageMapper.read(titleImage) : null)
                .build();
    }

    @Override
    public CarFullDto readFull(Car car) {
        Image titleImage = car.getTitleImage();
        Collection<Image> images = car.getImages();
        CarInformation information = car.getInformation();

        return CarFullDto.builder()
                .id(car.getId())
                .model(car.getModel())
                .available(car.isAvailable())
                .year(car.getYear())
                .licensePlate(car.getLicensePlate())
                .make(makeMapper.readShort(car.getMake()))
                .carType(carTypeMapper.read(car.getCarType()))
                .engine(engineMapper.read(car.getEngine()))
                .information(readInformation(information))
                .titleImage(titleImage != null ? imageMapper.read(titleImage) : null)
                .images(images != null ? images.stream().map(imageMapper::read).toList() : List.of())
                .build();
    }

    private CarInformationDto readInformation(CarInformation information) {
        if (information == null) {
            return null;
        }

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
    public PageDto<CarDto> readPage(Page<Car> found) {
        boolean hasNext = found.hasNext();
        boolean hasPrevious = found.hasPrevious();
        return new PageDto<>(
                found.getContent().stream().map(this::read).toList(),
                found.getNumber(),
                found.getSize(),
                found.getTotalPages(),
                hasNext ? found.nextPageable().getPageNumber() : 0,
                hasPrevious ? found.previousPageable().getPageNumber() : 0,
                hasNext ? found.nextOrLastPageable().getPageNumber() : 0,
                hasPrevious ? found.previousOrFirstPageable().getPageNumber() : 0
        );
    }

    @Override
    public CarUpdateDto readUpdate(Car car) {
        return CarUpdateDto.builder()
                .makeId(car.getMake().getId())
                .model(car.getModel())
                .year(car.getYear())
//                .titleImageId(car.getTitleImage() != null ? car.getTitleImage().getId() : null)
                .licensePlate(car.getLicensePlate())
//                .images(imagesMapper.readUpdateImages(car.getImages()))
                .build();
    }

    @Override
    public Car update(CarUpdateDto car, int id) {
        return Car.builder()
                .id(id)
//                .make(makeRepo.getReferenceById(car.getMakeId()))
                .model(car.getModel())
//                .titleImage(imageRepo.getReferenceById(car.getTitleImageId()))
//                .images(imageRepo.findAllByCarId(id))
                .licensePlate(car.getLicensePlate())
                .year(car.getYear())
                .build();
    }
}
