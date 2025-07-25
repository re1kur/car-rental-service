package re1kur.app.mapper.impl;

import re1kur.app.core.annotations.Mapper;
import re1kur.app.core.dto.FileDto;
import re1kur.app.core.dto.ImageDto;
import re1kur.app.entity.image.Image;
import re1kur.app.mapper.ImageMapper;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper
public class ImageMapperImpl implements ImageMapper {
    @Override
    public Image write(FileDto response) {
        return Image.builder()
                .id(response.id())
                .url(response.url())
                .uploadedAt(response.uploadedAt())
                .urlExpiresAt(response.urlExpiresAt())
                .build();
    }

    @Override
    public ImageDto read(Image titleImage) {
        if (titleImage == null) {
            return null;
        }

        return ImageDto.builder()
                .id(titleImage.getId())
                .url(titleImage.getUrl())
                .uploadedAt(LocalDateTime.ofInstant(titleImage.getUploadedAt(), ZoneId.systemDefault()))
                .build();
    }
}
