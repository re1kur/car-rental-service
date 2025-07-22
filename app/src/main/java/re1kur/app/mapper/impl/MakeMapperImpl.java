package re1kur.app.mapper.impl;

import re1kur.app.core.annotations.Mapper;
import re1kur.app.core.dto.MakeFullDto;
import re1kur.app.core.dto.MakeDto;
import re1kur.app.core.dto.MakeShortDto;
import re1kur.app.core.payload.MakeUpdatePayload;
import re1kur.app.core.payload.MakePayload;
import re1kur.app.entity.image.Image;
import re1kur.app.entity.make.Make;
import re1kur.app.entity.make.MakeInformation;
import re1kur.app.mapper.MakeMapper;

@Mapper
public class MakeMapperImpl implements MakeMapper {

    @Override
    public MakeFullDto readFull(Make make) {
        MakeInformation makeInformation = make.getMakeInformation();
        Image titleImage = make.getTitleImage();
        MakeFullDto build = MakeFullDto.builder()
                .id(make.getId())
                .name(make.getName())
                .titleImgUrl(titleImage != null ? titleImage.getUrl() : null)
                .build();
        if (makeInformation != null) {
            build.setDescription(makeInformation.getDescription());
            build.setCountry(makeInformation.getCountry());
            build.setFounder(makeInformation.getFounder());
            build.setOwner(makeInformation.getOwner());
            build.setFoundedAt(makeInformation.getFoundedAt());
        }
        return build;
    }

    @Override
    public Make write(MakePayload payload, Image image) {
        return Make.builder()
                .name(payload.name())
                .titleImage(image)
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
