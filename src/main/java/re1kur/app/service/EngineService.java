package re1kur.app.service;

import org.springframework.data.domain.Pageable;
import re1kur.app.core.dto.EngineDto;
import re1kur.app.core.dto.PageDto;
import re1kur.app.core.payload.EnginePayload;
import re1kur.app.core.payload.EngineUpdatePayload;
import re1kur.app.entity.Engine;

import java.util.List;

public interface EngineService {
    Integer create(EnginePayload payload);

    EngineDto read(Integer id);

    void update(EngineUpdatePayload payload, Integer id);

    void delete(Integer id);

    PageDto<EngineDto> readPage(Pageable pageable);

    List<EngineDto> readAll();

    Engine get(Integer id);
}
