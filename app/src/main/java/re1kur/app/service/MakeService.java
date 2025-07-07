package re1kur.app.service;

import re1kur.app.core.dto.MakeDto;
import re1kur.app.core.payload.MakeUpdatePayload;
import re1kur.app.core.payload.MakePayload;

import java.util.List;

public interface MakeService {

    List<MakeDto> readAll();

    void write(MakePayload make);

    MakeDto get(Integer id);

    void update(MakeUpdatePayload update, Integer id);
}
