package re1kur.rentalservice.mapper;

import re1kur.rentalservice.dto.make.MakeReadDto;
import re1kur.rentalservice.entity.Make;

public interface MakeMapper {
    MakeReadDto read (Make make);
}
