package re1kur.app.mapper;

import re1kur.app.core.dto.MakeFullDto;
import re1kur.app.core.dto.MakeDto;
import re1kur.app.core.dto.MakeShortDto;
import re1kur.app.core.payload.MakeUpdatePayload;
import re1kur.app.core.payload.MakePayload;
import re1kur.app.entity.image.Image;
import re1kur.app.entity.make.Make;

public interface MakeMapper {

    Make write (MakePayload make, Image mayBeImage);

    MakeUpdatePayload readUpdate(Make make);

    Make update(MakeUpdatePayload make, int id);

    MakeFullDto readFull(Make make);

    MakeDto read(Make make);

    MakeShortDto readShort(Make make);
}
