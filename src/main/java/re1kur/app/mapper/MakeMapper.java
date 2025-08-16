package re1kur.app.mapper;

import re1kur.app.core.dto.MakeFullDto;
import re1kur.app.core.dto.MakeDto;
import re1kur.app.core.dto.MakeShortDto;
import re1kur.app.core.payload.MakeUpdatePayload;
import re1kur.app.core.payload.MakePayload;
import re1kur.app.entity.Make;

public interface MakeMapper {

    Make create(MakePayload make);

    Make update(Make make, MakeUpdatePayload payload);

    MakeFullDto readFull(Make make);

    MakeDto read(Make make);

    MakeShortDto readShort(Make make);
}
