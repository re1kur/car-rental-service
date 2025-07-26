package re1kur.app.service;

import org.springframework.web.multipart.MultipartFile;
import re1kur.app.core.dto.MakeFullDto;
import re1kur.app.core.dto.MakeDto;
import re1kur.app.core.payload.MakeUpdatePayload;
import re1kur.app.core.payload.MakePayload;
import re1kur.app.entity.make.Make;

import java.util.List;

public interface MakeService {

    List<MakeDto> readAll();

    void create(MakePayload make, MultipartFile titleImg, MultipartFile[] files);

    MakeFullDto read(Integer id);

    void update(MakeUpdatePayload update, Integer id);

    Make get(Integer id);
}
