package re1kur.app.mapper.make;

import re1kur.app.model.dto.MakeInformationDto;
import re1kur.app.model.payload.MakePayload;
import re1kur.app.model.payload.MakeUpdatePayload;
import re1kur.app.model.entity.Make;
import re1kur.app.model.entity.MakeInformation;

public interface MakeInformationMapper {
    MakeInformation write(MakePayload payload, Make saved);

    MakeInformationDto read(MakeInformation makeInformation);

    MakeInformation update(Make make, MakeUpdatePayload payload);
}
