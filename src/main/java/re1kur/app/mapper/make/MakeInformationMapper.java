package re1kur.app.mapper.make;

import re1kur.app.dto.view.MakeInformationView;
import re1kur.app.dto.payload.MakePayload;
import re1kur.app.dto.payload.MakeUpdatePayload;
import re1kur.app.entity.Make;
import re1kur.app.entity.MakeInformation;

public interface MakeInformationMapper {
    MakeInformation write(MakePayload payload, Make saved);

    MakeInformationView read(MakeInformation makeInformation);

    MakeInformation update(Make make, MakeUpdatePayload payload);
}
