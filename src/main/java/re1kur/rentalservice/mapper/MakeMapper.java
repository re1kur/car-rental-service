package re1kur.rentalservice.mapper;

import re1kur.rentalservice.core.dto.make.MakeReadDto;
import re1kur.rentalservice.core.dto.make.MakeUpdateDto;
import re1kur.rentalservice.core.dto.make.MakeWriteDto;
import re1kur.rentalservice.entity.Make;

public interface MakeMapper {
    MakeReadDto read (Make make);

    Make write (MakeWriteDto make);

    MakeUpdateDto readUpdate(Make make);

    Make update(MakeUpdateDto make, int id);
}
