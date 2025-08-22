package re1kur.app.mapper.impl;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import re1kur.app.core.dto.EngineDto;
import re1kur.app.core.dto.PageDto;
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

    @Override
    public PageDto<EngineDto> readPage(Page<Engine> page) {
        boolean hasNext = page.hasNext();
        boolean hasPrevious = page.hasPrevious();
        return new PageDto<>(
                page.getContent().stream().map(this::read).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                hasNext ? page.nextPageable().getPageNumber() : 0,
                hasPrevious ? page.previousPageable().getPageNumber() : 0,
                hasNext ? page.nextOrLastPageable().getPageNumber() : 0,
                hasPrevious ? page.previousOrFirstPageable().getPageNumber() : 0
        );
    }
}
