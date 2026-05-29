package re1kur.app.mapper.car;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import re1kur.app.mapper.file.FileMapper;
import re1kur.app.mapper.make.MakeMapper;
import re1kur.app.util.annotations.Mapper;
import re1kur.app.dto.view.CarView;
import re1kur.app.dto.payload.CarUpdatePayload;
import re1kur.app.dto.payload.CarPayload;
import re1kur.app.dto.view.CarFullView;
import re1kur.app.dto.view.CarUpdateView;
import re1kur.app.dto.view.PageView;
import re1kur.app.entity.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;


@Mapper
@RequiredArgsConstructor
public class CarMapperImpl implements CarMapper {
    private final CarInformationMapper infoMapper;
    private final MakeMapper makeMapper;
    private final FileMapper imageMapper;

    @Override
    public Car write(CarPayload payload, Make make) {
        return Car.builder()
                .make(make)
                .carType(payload.carType())
                .engine(payload.engine())
                .model(payload.model())
                .year(payload.year())
                .licensePlate(payload.licensePlate())
                .isAvailable(payload.available() == null || payload.available())
                .cost(payload.cost())
                .build();
    }

    @Override
    public CarView read(Car car) {
        File titleImage = car.getTitleImage();
        return CarView.builder()
                .id(car.getId())
                .model(car.getModel())
                .year(car.getYear())
                .licensePlate(car.getLicensePlate())
                .cost(car.getCost())
                .available(car.isAvailable())
                .make(makeMapper.readShort(car.getMake()))
                .carType(car.getCarType())
                .engine(car.getEngine())
                .titleImage(imageMapper.read(titleImage))
                .build();
    }

    @Override
    public CarFullView readFull(Car car) {
        File titleImage = car.getTitleImage();
        Collection<File> images = car.getImages();

        return CarFullView.builder()
                .id(car.getId())
                .model(car.getModel())
                .available(car.isAvailable())
                .year(car.getYear())
                .licensePlate(car.getLicensePlate())
                .make(makeMapper.readShort(car.getMake()))
                .carType(car.getCarType())
                .engine(car.getEngine())
                .information(infoMapper.read(car.getInformation()))
                .titleImage(imageMapper.read(titleImage))
                .images(images != null ? images.stream().map(imageMapper::read).toList() : List.of())
                .cost(car.getCost())
                .build();
    }


    @Override
    public PageView<CarView> readPage(Page<Car> found) {
        boolean hasNext = found.hasNext();
        boolean hasPrevious = found.hasPrevious();
        return new PageView<>(
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
    public CarUpdateView readUpdate(Car car) {
        File titleImage = car.getTitleImage();
        Collection<File> images = car.getImages();

        return CarUpdateView.builder()
                .id(car.getId())
                .model(car.getModel())
                .year(car.getYear())
                .licensePlate(car.getLicensePlate())
                .make(makeMapper.readShort(car.getMake()))
                .carType(car.getCarType())
                .engine(car.getEngine())
                .information(infoMapper.read(car.getInformation()))
                .titleImage(imageMapper.read(titleImage))
                .images(images != null ? images.stream().map(imageMapper::read).toList() : List.of())
                .available(car.isAvailable())
                .cost(car.getCost())
                .build();
    }

    @Override
    public Car update(Car found, CarUpdatePayload payload, Make make) {
        String titleImageId = payload.titleImageId();
        File titleImage = found.getTitleImage();

        found.setMake(make);
        found.setCarType(payload.carType());
        found.setEngine(payload.engine());
        found.setLicensePlate(payload.licensePlate());
        found.setModel(payload.model());
        found.setInformation(infoMapper.update(payload, found));
        found.setAvailable(payload.available());
        found.setCost(payload.cost());

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
