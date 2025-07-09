package re1kur.app.mapper;

import re1kur.app.core.payload.MakePayload;
import re1kur.app.entity.car.Make;
import re1kur.app.entity.car.MakeInformation;

public interface MakeInformationMapper {
    MakeInformation write(MakePayload payload, Make saved);
}
