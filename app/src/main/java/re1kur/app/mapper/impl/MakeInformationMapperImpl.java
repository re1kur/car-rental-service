package re1kur.app.mapper.impl;

import re1kur.app.core.annotations.Mapper;
import re1kur.app.core.payload.MakePayload;
import re1kur.app.entity.make.Make;
import re1kur.app.entity.make.MakeInformation;
import re1kur.app.mapper.MakeInformationMapper;

@Mapper
public class MakeInformationMapperImpl implements MakeInformationMapper {
    @Override
    public MakeInformation write(MakePayload payload, Make saved) {
        MakeInformation build = MakeInformation.builder()
                .make(saved)
                .country(payload.country())
                .description(payload.description())
                .foundedAt(payload.foundedAt())
                .founder(payload.founder())
                .owner(payload.owner())
                .build();

        saved.setMakeInformation(build);

        return build;
    }
}
