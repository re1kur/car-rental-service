package re1kur.app.mapper.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import re1kur.app.core.annotations.Mapper;
import re1kur.app.core.dto.MakeFullDto;
import re1kur.app.core.dto.MakeDto;
import re1kur.app.core.dto.MakeShortDto;
import re1kur.app.core.payload.MakeUpdatePayload;
import re1kur.app.core.payload.MakePayload;
import re1kur.app.entity.File;
import re1kur.app.entity.Make;
import re1kur.app.entity.MakeInformation;
import re1kur.app.mapper.FileMapper;
import re1kur.app.mapper.MakeInformationMapper;
import re1kur.app.mapper.MakeMapper;

import java.util.List;
import java.util.Objects;

@Slf4j
@Mapper
@RequiredArgsConstructor
public class MakeMapperImpl implements MakeMapper {
    private final FileMapper imageMapper;
    private final MakeInformationMapper infoMapper;

    @Override
    public MakeFullDto readFull(Make make) {
        MakeInformation makeInformation = make.getInformation();
        File titleImage = make.getTitleImage();
        List<File> images = make.getImages();

        return MakeFullDto.builder()
                .id(make.getId())
                .name(make.getName())
                .information(infoMapper.read(makeInformation))
                .titleImage(imageMapper.read(titleImage))
                .images(images != null ? images.stream().map(imageMapper::read).toList() : List.of())
                .build();
    }

    @Override
    public Make create(MakePayload payload) {
        return Make.builder()
                .name(payload.name())
                .build();
    }

    @Override
    public Make update(Make make, MakeUpdatePayload payload) {
        String newImageId = payload.titleImageId();
        File titleImage = make.getTitleImage();

        make.setName(payload.name());
        make.setInformation(infoMapper.update(make, payload));

        if ((titleImage == null && newImageId != null)
                || (titleImage != null && !Objects.equals(titleImage.getId(), newImageId))
        ) {
            File image = make.getImages().stream()
                    .filter(img -> img.getId().equals(newImageId))
                    .findFirst().orElse(null);

            make.setTitleImage(image);
        }

        return make;
    }

    @Override
    public MakeDto read(Make make) {
        File titleImage = make.getTitleImage();
        return MakeDto.builder()
                .id(make.getId())
                .name(make.getName())
                .titleImgUrl(titleImage != null ? titleImage.getUrl() : null)
                .build();
    }

    @Override
    public MakeShortDto readShort(Make make) {
        return MakeShortDto.builder()
                .id(make.getId())
                .name(make.getName())
                .build();
    }
}