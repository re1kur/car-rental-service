package re1kur.app.mapper;

import org.springframework.data.domain.Page;
import re1kur.app.core.dto.EngineDto;
import re1kur.app.core.dto.PageDto;
import re1kur.app.core.payload.EnginePayload;
import re1kur.app.core.payload.EngineUpdatePayload;
import re1kur.app.entity.Engine;

public interface EngineMapper {
    Engine write(EnginePayload payload);

    EngineDto read(Engine engine);

    Engine update(Engine engine, EngineUpdatePayload payload);

    PageDto<EngineDto> readPage(Page<Engine> page);
}
