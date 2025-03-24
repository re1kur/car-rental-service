package re1kur.rentalservice.mapper.impl;

import re1kur.rentalservice.annotations.Mapper;
import re1kur.rentalservice.dto.make.MakeReadDto;
import re1kur.rentalservice.dto.make.MakeUpdateDto;
import re1kur.rentalservice.dto.make.MakeWriteDto;
import re1kur.rentalservice.entity.Make;
import re1kur.rentalservice.mapper.MakeMapper;

@Mapper
public class DefaultMakeMapper implements MakeMapper {

    @Override
    public MakeReadDto read(Make make) {
        return MakeReadDto.builder()
                .id(make.getId())
                .name(make.getName())
                .country(make.getCountry())
                .description(make.getDescription())
                .build();
    }

    @Override
    public Make write(MakeWriteDto make) {
        return Make.builder()
                .name(make.getName())
                .country(make.getCountry())
                .description(make.getDescription())
                .build();
    }

    @Override
    public MakeUpdateDto readUpdate(Make make) {
        return MakeUpdateDto.builder()
                .name(make.getName())
                .country(make.getCountry())
                .description(make.getDescription())
                .build();
    }

    @Override
    public Make update(MakeUpdateDto make, int id) {
        return Make.builder()
                .id(id)
                .name(make.getName())
                .country(make.getCountry())
                .description(make.getDescription())
                .build();
    }

}
