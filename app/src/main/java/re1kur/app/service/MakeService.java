package re1kur.app.service;

import org.springframework.web.multipart.MultipartFile;
import re1kur.app.core.dto.MakeDto;
import re1kur.app.core.dto.MakeShortDto;
import re1kur.app.core.payload.MakeUpdatePayload;
import re1kur.app.core.payload.MakePayload;

import java.util.List;

public interface MakeService {

    List<MakeShortDto> readAll();

    void create(MakePayload make, MultipartFile titleImg);

    MakeDto get(Integer id);

    void update(MakeUpdatePayload update, Integer id);
}
