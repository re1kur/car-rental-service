package re1kur.app.mapper;

import re1kur.app.core.dto.MakeDto;
import re1kur.app.core.payload.MakeUpdatePayload;
import re1kur.app.core.payload.MakePayload;
import re1kur.app.entity.car.Make;

public interface MakeMapper {
    MakeDto read (Make make);

    Make write (MakePayload make);

    MakeUpdatePayload readUpdate(Make make);

    Make update(MakeUpdatePayload make, int id);
}
