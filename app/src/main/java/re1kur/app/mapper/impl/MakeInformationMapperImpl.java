package re1kur.app.mapper.impl;

import re1kur.app.core.annotations.Mapper;
import re1kur.app.core.dto.MakeInformationDto;
import re1kur.app.core.payload.MakePayload;
import re1kur.app.entity.make.Make;
import re1kur.app.entity.make.MakeInformation;
import re1kur.app.mapper.MakeInformationMapper;

import java.time.LocalDate;

@Mapper
public class MakeInformationMapperImpl implements MakeInformationMapper {
    @Override
    public MakeInformation write(MakePayload payload, Make make) {
        MakeInformation information = MakeInformation.builder().make(make).build();

        boolean hasInfo = false;
        String description = payload.description();
        String country = payload.country();
        LocalDate foundedAt = payload.foundedAt();
        String owner = payload.owner();
        String founder = payload.founder();

        if (isNotEmpty(description)) {
            information.setDescription(description);
            hasInfo = true;
        }
        if (isNotEmpty(country)) {
            information.setCountry(country);
            hasInfo = true;
        }
        if (foundedAt != null) {
            information.setFoundedAt(foundedAt);
            hasInfo = true;
        }
        if (isNotEmpty(owner)) {
            information.setOwner(owner);
            hasInfo = true;
        }
        if (isNotEmpty(founder)) {
            information.setFounder(founder);
            hasInfo = true;
        }

        if (hasInfo) {
            return information;
        }

        return null;
    }

    private static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }


    @Override
    public MakeInformationDto read(MakeInformation information) {
        if (information == null)
            return null;

        return MakeInformationDto.builder()
                .country(information.getCountry())
                .description(information.getDescription())
                .foundedAt(information.getFoundedAt())
                .founder(information.getFounder())
                .owner(information.getOwner())
                .build();
    }
}
