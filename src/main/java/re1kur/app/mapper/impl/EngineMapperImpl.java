package re1kur.app.mapper.impl;

import org.springframework.stereotype.Component;
import re1kur.app.core.dto.EngineDto;
import re1kur.app.core.payload.EnginePayload;
import re1kur.app.core.payload.EngineUpdatePayload;
import re1kur.app.entity.Engine;
import re1kur.app.mapper.EngineMapper;

@Component
public class EngineMapperImpl implements EngineMapper {
    @Override
    public Engine write(EnginePayload payload) {
        return Engine.builder()
                .name(payload.name())
                .build();
    }

    @Override
    public EngineDto read(Engine engine) {
        return EngineDto.builder()
                .id(engine.getId())
                .name(engine.getName())
                .build();
    }

    @Override
    public Engine update(Engine engine, EngineUpdatePayload payload) {
        engine.setName(payload.name());

        return engine;
    }
}
