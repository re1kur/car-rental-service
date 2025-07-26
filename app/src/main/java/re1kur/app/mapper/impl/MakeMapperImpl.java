package re1kur.app.mapper.impl;

import lombok.RequiredArgsConstructor;
import re1kur.app.core.annotations.Mapper;
import re1kur.app.core.dto.MakeFullDto;
import re1kur.app.core.dto.MakeDto;
import re1kur.app.core.dto.MakeShortDto;
import re1kur.app.core.payload.MakeUpdatePayload;
import re1kur.app.core.payload.MakePayload;
import re1kur.app.entity.image.Image;
import re1kur.app.entity.make.Make;
import re1kur.app.entity.make.MakeInformation;
import re1kur.app.mapper.ImageMapper;
import re1kur.app.mapper.MakeInformationMapper;
import re1kur.app.mapper.MakeMapper;

import java.util.List;

@Mapper
@RequiredArgsConstructor
public class MakeMapperImpl implements MakeMapper {
    private final ImageMapper imageMapper;
    private final MakeInformationMapper infoMapper;

    @Override
    public MakeFullDto readFull(Make make) {
        MakeInformation makeInformation = make.getInformation();
        Image titleImage = make.getTitleImage();
        List<Image> images = make.getImages();

        return MakeFullDto.builder()
                .id(make.getId())
                .name(make.getName())
                .information(infoMapper.read(makeInformation))
                .titleImage(imageMapper.read(titleImage))
                .images(images != null ? images.stream().map(imageMapper::read).toList() : List.of())
                .build();
    }

    @Override
    public Make write(MakePayload payload) {
        return Make.builder()
                .name(payload.name())
                .build();
    }

    @Override
    public MakeUpdatePayload readUpdate(Make make) {
        return MakeUpdatePayload.builder()
                .name(make.getName())
//                .country(make.getCountry())
//                .description(make.getDescription())
//                .titleImageUrl(make.getTitleImageUrl())
                .build();
    }

    @Override
    public Make update(MakeUpdatePayload make, int id) {
        return Make.builder()
                .id(id)
                .name(make.getName())
//                .country(make.getCountry())
//                .description(make.getDescription())
//                .titleImageUrl(make.getTitleImageUrl())
                .build();
    }

    @Override
    public MakeDto read(Make make) {
        Image titleImage = make.getTitleImage();
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
