package re1kur.app.mapper.impl;

import re1kur.app.annotations.Mapper;
import re1kur.app.dto.make.MakeReadDto;
import re1kur.app.dto.make.MakeUpdateDto;
import re1kur.app.dto.make.MakeWriteDto;
import re1kur.app.entity.Make;
import re1kur.app.mapper.MakeMapper;

@Mapper
public class DefaultMakeMapper implements MakeMapper {

    @Override
    public MakeReadDto read(Make make) {
        return MakeReadDto.builder()
                .id(make.getId())
                .name(make.getName())
                .country(make.getCountry())
                .description(make.getDescription())
                .titleImageUrl(make.getTitleImageUrl())
                .build();
    }

    @Override
    public Make write(MakeWriteDto make) {
        return Make.builder()
                .name(make.getName())
                .country(make.getCountry())
                .description(make.getDescription())
                .titleImageUrl(make.getTitleImageUrl())
                .build();
    }

    @Override
    public MakeUpdateDto readUpdate(Make make) {
        return MakeUpdateDto.builder()
                .name(make.getName())
                .country(make.getCountry())
                .description(make.getDescription())
                .titleImageUrl(make.getTitleImageUrl())
                .build();
    }

    @Override
    public Make update(MakeUpdateDto make, int id) {
        return Make.builder()
                .id(id)
                .name(make.getName())
                .country(make.getCountry())
                .description(make.getDescription())
                .titleImageUrl(make.getTitleImageUrl())
                .build();
    }

}
