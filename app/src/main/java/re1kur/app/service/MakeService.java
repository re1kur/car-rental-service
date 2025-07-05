package re1kur.app.service;

import re1kur.app.core.make.MakeReadDto;
import re1kur.app.core.make.MakeUpdateDto;
import re1kur.app.core.make.MakeWriteDto;

import java.util.List;

public interface MakeService {

    List<MakeReadDto> readAll();

    MakeReadDto write(MakeWriteDto make);

    MakeReadDto read(int id);

    MakeUpdateDto readUpdateById(int id);

    void updateMake(MakeUpdateDto update, int id);
}
