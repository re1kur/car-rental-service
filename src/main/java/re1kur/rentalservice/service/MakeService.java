package re1kur.rentalservice.service;

import re1kur.rentalservice.dto.make.MakeReadDto;
import re1kur.rentalservice.dto.make.MakeUpdateDto;
import re1kur.rentalservice.dto.make.MakeWriteDto;

import java.util.List;

public interface MakeService {

    List<MakeReadDto> readAll();

    MakeReadDto write(MakeWriteDto make);

    MakeReadDto read(int id);

    MakeUpdateDto readUpdateById(int id);

    void updateMake(MakeUpdateDto update, int id);
}
