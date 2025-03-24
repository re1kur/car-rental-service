package re1kur.rentalservice.mapper.impl;

import lombok.extern.slf4j.Slf4j;
import re1kur.rentalservice.annotations.Mapper;
import re1kur.rentalservice.dto.car.images.CarImageReadDto;
import re1kur.rentalservice.dto.car.images.CarImageWriteDto;
import re1kur.rentalservice.entity.CarImage;
import re1kur.rentalservice.mapper.CarImagesMapper;
import re1kur.rentalservice.service.impl.DefaultFileStoreService;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
@Mapper
public class DefaultCarImagesMapper implements CarImagesMapper {
    private final DefaultFileStoreService defaultFileStoreService;

    public DefaultCarImagesMapper(DefaultFileStoreService defaultFileStoreService) {
        this.defaultFileStoreService = defaultFileStoreService;
    }

    @Override
    public List<CarImage> writeImages(List<CarImageWriteDto> images) {
        if (images == null || images.isEmpty()) {
            return null;
        }
        return images.stream().map(image -> CarImage.builder()
                        .imageUrl(image.getUrl())
                        .build())
                .toList();
    }

    @Override
    public CarImage write(CarImageWriteDto car) {
        return CarImage.builder()
                .imageUrl(car.getUrl())
                .build();
    }

    @Override
    public List<CarImageReadDto> readImages(Collection<CarImage> images) {
        if (images == null || images.isEmpty()) {
            return null;
        }
        return images.stream().map(image -> CarImageReadDto.builder()
                .id(image.getId())
                .carId(image.getCar().getId())
                .url(image.getImageUrl())
                .uploadedAt(image.getUploadedAt())
                .build())
                .filter(Objects::nonNull)
                .toList();
    }
}
