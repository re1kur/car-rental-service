package re1kur.app.mapper.impl;

import re1kur.app.core.annotations.Mapper;
import re1kur.app.core.dto.MakeDto;
import re1kur.app.core.payload.MakeUpdatePayload;
import re1kur.app.core.payload.MakePayload;
import re1kur.app.entity.car.Make;
import re1kur.app.mapper.MakeMapper;

@Mapper
public class DefaultMakeMapper implements MakeMapper {

    @Override
    public MakeDto read(Make make) {
        return MakeDto.builder()
                .id(make.getId())
                .name(make.getName())
//                .country(make.getCountry())
//                .description(make.getDescription())
//                .titleImageUrl(make.getTitleImageUrl())
                .build();
    }

    @Override
    public Make write(MakePayload make) {
        return Make.builder()
//                .name(make.getName())
//                .country(make.getCountry())
//                .description(make.getDescription())
//                .titleImage(make.getTitleImageUrl())
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

}
