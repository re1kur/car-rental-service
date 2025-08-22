package re1kur.app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re1kur.app.core.dto.EngineDto;
import re1kur.app.core.dto.PageDto;
import re1kur.app.core.exception.EngineAlreadyExistsException;
import re1kur.app.core.exception.EngineNotFoundException;
import re1kur.app.core.payload.EnginePayload;
import re1kur.app.core.payload.EngineUpdatePayload;
import re1kur.app.entity.Engine;
import re1kur.app.mapper.EngineMapper;
import re1kur.app.repository.EngineRepository;
import re1kur.app.service.EngineService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EngineServiceImpl implements EngineService {
    private final EngineRepository repo;
    private final EngineMapper mapper;

    @Override
    @Transactional
    public void create(EnginePayload payload) {
        log.info("Create engine request: {}", payload.toString());
        String name = payload.name();

        if (repo.existsByName(name))
            throw new EngineAlreadyExistsException("Engine with name '%s' already exists.".formatted(name));

        Engine mapped = mapper.write(payload);

        repo.save(mapped);

        log.info("Created engine : {}", payload);
    }

    @Override
    public EngineDto read(Integer id) {
        return repo.findById(id).map(mapper::read).orElseThrow(() ->
                new EngineNotFoundException("Engine [%d] was not found.".formatted(id)));
    }

    @Override
    @Transactional
    public void update(EngineUpdatePayload payload, Integer id) {
        log.info("Update engine request: {}", payload.toString());

        Engine engine = repo.findById(id)
                .orElseThrow(() -> new EngineNotFoundException("Engine [%d] was not found.".formatted(id)));

        if (!engine.getName().equals(payload.name())) {
            if (repo.existsByName(payload.name())) {
                throw new EngineAlreadyExistsException("Engine [%s] already exists.".formatted(payload.name()));
            }
        }

        Engine mapped = mapper.update(engine, payload);

        repo.save(mapped);

        log.info("Engine has been updated: {}", payload);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.info("Delete engine with ID [{}] request.", id);

        Engine engine = repo.findById(id)
                .orElseThrow(() -> new EngineNotFoundException("Engine [%d] was not found.".formatted(id)));

        repo.delete(engine);

        log.info("Engine [{}] has been deleted.", id);
    }

    @Override
    public PageDto<EngineDto> readPage(Pageable pageable) {
        return mapper.readPage(repo.findAll(pageable));
    }

    @Override
    public List<EngineDto> readAll() {
        List<Engine> found = (List<Engine>) repo.findAll();
        return found.stream().map(mapper::read).toList();
    }

    @Override
    public Engine get(Integer id) {
        return repo.findById(id).orElseThrow(() ->
                new EngineNotFoundException("Engine [%d] was not found.".formatted(id)));
    }

}
