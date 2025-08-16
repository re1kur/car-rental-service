package re1kur.app.mapper.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import re1kur.app.core.annotations.Mapper;
import re1kur.app.core.dto.CarDto;
import re1kur.app.core.payload.CarUpdatePayload;
import re1kur.app.core.dto.*;
import re1kur.app.core.payload.CarPayload;
import re1kur.app.entity.*;
import re1kur.app.mapper.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;


@Mapper
@RequiredArgsConstructor
public class CarMapperImpl implements CarMapper {
    private final CarInformationMapper infoMapper;
    private final MakeMapper makeMapper;
    private final CarTypeMapper carTypeMapper;
    private final EngineMapper engineMapper;
    private final FileMapper imageMapper;

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
        File titleImage = car.getTitleImage();
        return CarDto.builder()
                .id(car.getId())
                .model(car.getModel())
                .year(car.getYear())
                .licensePlate(car.getLicensePlate())
                .make(makeMapper.readShort(car.getMake()))
                .carType(carTypeMapper.read(car.getCarType()))
                .engine(engineMapper.read(car.getEngine()))
                .titleImage(imageMapper.read(titleImage))
                .build();
    }

    @Override
    public CarFullDto readFull(Car car) {
        File titleImage = car.getTitleImage();
        Collection<File> images = car.getImages();

        return CarFullDto.builder()
                .id(car.getId())
                .model(car.getModel())
                .available(car.isAvailable())
                .year(car.getYear())
                .licensePlate(car.getLicensePlate())
                .make(makeMapper.readShort(car.getMake()))
                .carType(carTypeMapper.read(car.getCarType()))
                .engine(engineMapper.read(car.getEngine()))
                .information(infoMapper.read(car.getInformation()))
                .titleImage(imageMapper.read(titleImage))
                .images(images != null ? images.stream().map(imageMapper::read).toList() : List.of())
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
        File titleImage = car.getTitleImage();
        Collection<File> images = car.getImages();

        return CarUpdateDto.builder()
                .id(car.getId())
                .model(car.getModel())
                .year(car.getYear())
                .licensePlate(car.getLicensePlate())
                .make(makeMapper.readShort(car.getMake()))
                .carTypeId(car.getCarType().getId())
                .engineId(car.getEngine().getId())
                .information(infoMapper.read(car.getInformation()))
                .titleImage(imageMapper.read(titleImage))
                .images(images != null ? images.stream().map(imageMapper::read).toList() : List.of())
                .build();
    }

    @Override
    public Car update(Car found, CarUpdatePayload payload, Make make, CarType type, Engine engine) {
        String titleImageId = payload.titleImageId();
        File titleImage = found.getTitleImage();

        found.setMake(make);
        found.setCarType(type);
        found.setEngine(engine);
        found.setLicensePlate(payload.licensePlate());
        found.setModel(payload.model());
        found.setInformation(infoMapper.update(payload, found));

        if ((titleImage == null && titleImageId != null)
                || (titleImage != null && !Objects.equals(titleImage.getId(), titleImageId))) {

            File image = found.getImages().stream()
                    .filter(img -> img.getId().equals(titleImageId))
                    .findFirst()
                    .orElse(null);

            found.setTitleImage(image);
        }

        return found;
    }
}
