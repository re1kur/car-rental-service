package re1kur.app.mapper.car;

import org.springframework.data.domain.Page;
import re1kur.app.model.dto.EngineDto;
import re1kur.app.model.dto.PageDto;
import re1kur.app.model.payload.EnginePayload;
import re1kur.app.model.payload.EngineUpdatePayload;
import re1kur.app.model.entity.Engine;

public interface EngineMapper {
    Engine write(EnginePayload payload);

    EngineDto read(Engine engine);

    Engine update(Engine engine, EngineUpdatePayload payload);

    PageDto<EngineDto> readPage(Page<Engine> page);
}
