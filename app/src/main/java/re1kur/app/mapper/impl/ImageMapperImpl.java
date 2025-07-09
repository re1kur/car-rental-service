package re1kur.app.mapper.impl;

import re1kur.app.core.annotations.Mapper;
import re1kur.app.core.dto.FileDto;
import re1kur.app.entity.Image;
import re1kur.app.mapper.ImageMapper;

import java.time.OffsetDateTime;
import java.time.ZoneId;

@Mapper
public class ImageMapperImpl implements ImageMapper {
    @Override
    public Image write(FileDto response) {
        return Image.builder()
                .id(response.id())
                .url(response.url())
                .uploadedAt(OffsetDateTime.ofInstant(response.uploadedAt(), ZoneId.systemDefault()))
                .urlExpiresAt(OffsetDateTime.ofInstant(response.urlExpiresAt(), ZoneId.systemDefault()))
                .build();
    }
}
