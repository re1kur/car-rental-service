//package re1kur.app.mapper.impl;
//
//import re1kur.app.core.annotations.Mapper;
//import re1kur.app.core.car.images.CarImageReadDto;
//import re1kur.app.core.car.images.CarImageUpdateDto;
//import re1kur.app.core.car.images.CarImageWriteDto;
////import re1kur.app.entity.CarImage;
////import re1kur.app.mapper.CarImagesMapper;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.Objects;
//
//@Mapper
//public class DefaultCarImagesMapper implements CarImagesMapper {
//
//    @Override
//    public List<CarImage> writeImages(List<CarImageWriteDto> images) {
//        if (images == null || images.isEmpty()) {
//            return null;
//        }
//        return images.stream().map(image -> CarImage.builder()
//                        .imageUrl(image.getUrl())
//                        .expiresAt(image.getExpiresAt())
//                        .build())
//                .toList();
//    }
//
//    @Override
//    public CarImage write(CarImageWriteDto car) {
//        return CarImage.builder()
//                .imageUrl(car.getUrl())
//                .expiresAt(car.getExpiresAt())
//                .build();
//    }
//
//    @Override
//    public List<CarImageReadDto> readImages(Collection<CarImage> images) {
//        if (images == null || images.isEmpty()) {
//            return null;
//        }
//        return images.stream().map(image -> CarImageReadDto.builder()
//                        .id(image.getId())
//                        .carId(image.getCar().getId())
//                        .url(image.getImageUrl())
//                        .uploadedAt(image.getUploadedAt())
//                        .expiresAt(image.getExpiresAt())
//                        .build())
//                .filter(Objects::nonNull)
//                .toList();
//    }
//
//    @Override
//    public CarImageReadDto read(CarImage image) {
//        if (image == null) {
//            return null;
//        }
//        return CarImageReadDto.builder()
//                .id(image.getId())
//                .carId(image.getCar().getId())
//                .url(image.getImageUrl())
//                .uploadedAt(image.getUploadedAt())
//                .expiresAt(image.getExpiresAt())
//                .build();
//    }
//
//    @Override
//    public List<CarImageUpdateDto> readUpdateImages(Collection<CarImage> images) {
//        if (images == null || images.isEmpty()) {
//            return null;
//        }
//        return images.stream().map(image -> CarImageUpdateDto.builder()
//                        .id(image.getId())
//                        .url(image.getImageUrl())
//                        .uploadedAt(image.getUploadedAt())
//                        .expiresAt(image.getExpiresAt())
//                        .build())
//                .filter(Objects::nonNull)
//                .toList();
//    }
//}
