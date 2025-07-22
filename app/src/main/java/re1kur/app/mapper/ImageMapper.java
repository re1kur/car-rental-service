package re1kur.app.mapper;

import re1kur.app.core.dto.FileDto;
import re1kur.app.core.dto.ImageDto;
import re1kur.app.entity.image.Image;

public interface ImageMapper {
    Image write(FileDto response);

    ImageDto read(Image titleImage);
}
