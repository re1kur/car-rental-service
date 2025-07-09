package re1kur.app.mapper;

import re1kur.app.core.dto.FileDto;
import re1kur.app.entity.Image;

public interface ImageMapper {
    Image write(FileDto response);
}
