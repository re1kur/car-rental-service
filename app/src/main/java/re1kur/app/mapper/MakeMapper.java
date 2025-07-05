package re1kur.app.mapper;

import re1kur.app.core.make.MakeReadDto;
import re1kur.app.core.make.MakeUpdateDto;
import re1kur.app.core.make.MakeWriteDto;
import re1kur.app.entity.Make;

public interface MakeMapper {
    MakeReadDto read (Make make);

    Make write (MakeWriteDto make);

    MakeUpdateDto readUpdate(Make make);

    Make update(MakeUpdateDto make, int id);
}
