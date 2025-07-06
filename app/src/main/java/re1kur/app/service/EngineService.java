package re1kur.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import re1kur.app.core.dto.EngineDto;
import re1kur.app.core.payload.EnginePayload;
import re1kur.app.core.payload.EngineUpdatePayload;

public interface EngineService {
    void create(EnginePayload payload);

    EngineDto get(Integer id);

    void update(EngineUpdatePayload payload, Integer id);

    void delete(Integer id);

    Page<EngineDto> getPage(Pageable pageable);

}
