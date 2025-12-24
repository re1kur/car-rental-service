package re1kur.rentalservice.mapper.impl;

import lombok.RequiredArgsConstructor;
import re1kur.rentalservice.core.annotation.Mapper;
import re1kur.rentalservice.core.dto.car.CarReadDto;
import re1kur.rentalservice.core.dto.car.CarUpdateDto;
import re1kur.rentalservice.core.dto.car.CarWriteDto;
import re1kur.rentalservice.entity.Car;
import re1kur.rentalservice.entity.CarDetails;
import re1kur.rentalservice.mapper.CarDetailsMapper;
import re1kur.rentalservice.mapper.CarMapper;
import re1kur.rentalservice.mapper.MakeMapper;
import re1kur.rentalservice.repository.MakeRepository;


@Mapper
@RequiredArgsConstructor
public class DefaultCarMapper implements CarMapper {
    private final MakeRepository makeRepo;
    //    CarImageRepository imageRepo;
    private final CarDetailsMapper detailsMapper;
    //    CarImagesMapper imagesMapper;
    private final MakeMapper makeMapper;


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
