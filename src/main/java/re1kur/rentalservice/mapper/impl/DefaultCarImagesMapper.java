package re1kur.rentalservice.mapper.impl;

import re1kur.rentalservice.dto.car.images.CarImageReadDto;
import re1kur.rentalservice.dto.car.images.CarImageWriteDto;
import re1kur.rentalservice.entity.CarImage;
import re1kur.rentalservice.mapper.CarImagesMapper;

import java.util.Collection;
import java.util.List;

public class DefaultCarImagesMapper implements CarImagesMapper {
    @Override
    public CarImage write(CarImageWriteDto writeCarImage) {
        return CarImage.builder().build();
    }

    @Override
    public List<CarImageReadDto> read(Collection<CarImage> images) {
        return images.stream().map(image -> CarImageReadDto.builder()
                .id(image.getId())
                .carId(image.getCar().getId())
                .url(image.getImageUrl())
                .uploadedAt(image.getUploadedAt())
                .build())
                .toList();
    }
}
