//package re1kur.rentalservice.mapper.impl;
//
//import re1kur.rentalservice.annotations.Mapper;
//import re1kur.rentalservice.dto.car.images.CarImageReadDto;
//import re1kur.rentalservice.dto.car.images.CarImageWriteDto;
//import re1kur.rentalservice.entity.Car;
//import re1kur.rentalservice.entity.CarImage;
//import re1kur.rentalservice.mapper.CarImagesMapper;
//
//import java.util.Collection;
//import java.util.List;
//
//@Mapper
//public class DefaultCarImagesMapper implements CarImagesMapper {
//    @Override
//    public List<CarImage> writeImages(List<CarImageWriteDto> images) {
//        if (images == null || images.isEmpty()) {
//            return null;
//        }
//        return images.stream().map(image -> CarImage.builder()
//                        .imageUrl(image.getUrl())
//                        .build())
//                .toList();
//    }
//
//    @Override
//    public CarImage writeImage(String url, Car car) {
//        return CarImage.builder()
//                .car(car)
//                .imageUrl(url)
//                .build();
//    }
//
//    @Override
//    public List<CarImageReadDto> read(Collection<CarImage> images) {
//        if (images == null || images.isEmpty()) {
//            return null;
//        }
//        return images.stream().map(image -> CarImageReadDto.builder()
//                .id(image.getId())
//                .carId(image.getCar().getId())
//                .url(image.getImageUrl())
//                .uploadedAt(image.getUploadedAt())
//                .build())
//                .toList();
//    }
//}
