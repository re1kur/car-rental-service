package re1kur.rentalservice.mapper;

import re1kur.rentalservice.dto.car.images.CarImageReadDto;
import re1kur.rentalservice.dto.car.images.CarImageWriteDto;
import re1kur.rentalservice.entity.CarImage;

import java.util.Collection;
import java.util.List;

public interface CarImagesMapper {
    CarImage write(CarImageWriteDto writeCarImage);

    List<CarImageReadDto> read(Collection<CarImage> images);
}
