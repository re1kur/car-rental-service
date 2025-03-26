package re1kur.rentalservice.mapper;

import re1kur.rentalservice.dto.car.images.CarImageReadDto;
import re1kur.rentalservice.dto.car.images.CarImageUpdateDto;
import re1kur.rentalservice.dto.car.images.CarImageWriteDto;
import re1kur.rentalservice.entity.CarImage;

import java.util.Collection;
import java.util.List;

public interface CarImagesMapper {
    List<CarImage> writeImages(List<CarImageWriteDto> carImageWriteDto);

    CarImage write(CarImageWriteDto carImageWriteDto);

    List<CarImageReadDto> readImages(Collection<CarImage> images);

    CarImageReadDto read(CarImage titleImage);

    List<CarImageUpdateDto> readUpdateImages(Collection<CarImage> images);
}
