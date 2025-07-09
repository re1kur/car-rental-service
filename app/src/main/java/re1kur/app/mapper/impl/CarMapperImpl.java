package re1kur.app.mapper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import re1kur.app.core.annotations.Mapper;
import re1kur.app.core.car.CarReadDto;
import re1kur.app.core.car.CarUpdateDto;
import re1kur.app.core.car.CarWriteDto;
import re1kur.app.entity.car.Car;
import re1kur.app.entity.car.CarDetails;
//import re1kur.app.entity.CarImage;
import re1kur.app.mapper.CarDetailsMapper;
//import re1kur.app.mapper.CarImagesMapper;
import re1kur.app.mapper.CarMapper;
import re1kur.app.mapper.MakeMapper;
//import re1kur.app.repository.CarImageRepository;
import re1kur.app.repository.MakeRepository;


@Mapper
public class CarMapperImpl implements CarMapper {
    MakeRepository makeRepo;
//    CarImageRepository imageRepo;
    CarDetailsMapper detailsMapper;
//    CarImagesMapper imagesMapper;
    MakeMapper makeMapper;

    @Autowired
    public CarMapperImpl(
            CarDetailsMapper detailsMapper,
//            CarImagesMapper imagesMapper,
            MakeRepository makeRepo,
            MakeMapper makeMapper
//            CarImageRepository imageRepo
    ) {
        this.detailsMapper = detailsMapper;
//        this.imagesMapper = imagesMapper;
        this.makeRepo = makeRepo;
        this.makeMapper = makeMapper;
//        this.imageRepo = imageRepo;
    }

    @Override
    public Car write(CarWriteDto car) {
        CarDetails details = detailsMapper.write(car.getDetails());

        Car build = Car.builder()
                .make(makeRepo.getReferenceById(car.getMakeId()))
                .model(car.getModel())
                .year(car.getYear())
                .licensePlate(car.getLicensePlate())
                .details(details)
                .build();

        if (details != null) {
            details.setCar(build);
        }

//        if (car.getTitleImage() != null) {
//            CarImage title = imagesMapper.write(car.getTitleImage());
//            build.addImage(title);
//            build.setTitleImage(title);
//        }
//        if (car.getImage() != null) {
//            CarImage image = imagesMapper.write(car.getImage());
//            build.addImage(image);
//        }
        return build;
    }

    @Override
    public CarReadDto read(Car car) {
        return CarReadDto.builder()
                .id(car.getId())
                .make(makeMapper.read(car.getMake()))
                .model(car.getModel())
                .year(car.getYear())
//                .titleImage(imagesMapper.read(car.getTitleImage()))
                .isAvailable(car.isAvailable())
                .licensePlate(car.getLicensePlate())
                .build();
    }

    @Override
    public CarReadDto readWithDetails(Car car) {
        return CarReadDto.builder()
                .id(car.getId())
                .make(makeMapper.read(car.getMake()))
                .model(car.getModel())
                .year(car.getYear())
                .isAvailable(car.isAvailable())
                .licensePlate(car.getLicensePlate())
                .details(detailsMapper.read(car.getDetails()))
//                .titleImage(imagesMapper.read(car.getTitleImage()))
//                .images(imagesMapper.readImages(car.getImages()))
                .build();
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
                .details(detailsMapper.readUpdate(car.getDetails()))
                .build();
    }

    @Override
    public Car update(CarUpdateDto car, int id) {
        return Car.builder()
                .id(id)
                .make(makeRepo.getReferenceById(car.getMakeId()))
                .model(car.getModel())
//                .titleImage(imageRepo.getReferenceById(car.getTitleImageId()))
//                .images(imageRepo.findAllByCarId(id))
                .licensePlate(car.getLicensePlate())
                .year(car.getYear())
                .details(detailsMapper.update(car.getDetails(), id))
                .build();
    }
}
