package re1kur.app.mapper;

import re1kur.app.core.dto.MakeInformationDto;
import re1kur.app.core.payload.MakePayload;
import re1kur.app.core.payload.MakeUpdatePayload;
import re1kur.app.entity.Make;
import re1kur.app.entity.MakeInformation;

public interface MakeInformationMapper {
    MakeInformation write(MakePayload payload, Make saved);

    MakeInformationDto read(MakeInformation makeInformation);

    MakeInformation update(Make make, MakeUpdatePayload payload);
}
