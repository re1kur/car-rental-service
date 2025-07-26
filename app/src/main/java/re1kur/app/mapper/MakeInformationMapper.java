package re1kur.app.mapper;

import re1kur.app.core.dto.MakeInformationDto;
import re1kur.app.core.payload.MakePayload;
import re1kur.app.entity.make.Make;
import re1kur.app.entity.make.MakeInformation;

public interface MakeInformationMapper {
    MakeInformation write(MakePayload payload, Make saved);

    MakeInformationDto read(MakeInformation makeInformation);
}
