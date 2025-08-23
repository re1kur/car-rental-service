package re1kur.app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
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
    public Integer create(EnginePayload payload, OidcUser user) {
        String logUser = user == null ? "Anonymous" : user.getSubject();
        log.info("CREATE ENGINE REQUEST [{}] BY USER [{}]", payload, logUser);
        String name = payload.name();

        if (repo.existsByName(name))
            throw new EngineAlreadyExistsException("Engine with name '%s' already exists.".formatted(name));

        Engine mapped = mapper.write(payload);

        Engine saved = repo.save(mapped);
        Integer engineId = saved.getId();

        log.info("CREATED ENGINE REQUEST [{}] BY USER [{}]", engineId, logUser);
        return engineId;
    }

    @Override
    public EngineDto read(Integer id, OidcUser user) {
        String logUser = user == null ? "Anonymous" : user.getSubject();
        log.info("READ ENGINE [{}] REQUEST BY USER [{}]", id, logUser);

        return repo.findById(id).map(mapper::read).orElseThrow(() ->
                new EngineNotFoundException("Engine [%d] was not found.".formatted(id)));
    }

    @Override
    @Transactional
    public void update(EngineUpdatePayload payload, Integer id, OidcUser user) {
        String logUser = user == null ? "Anonymous" : user.getSubject();
        log.info("UPDATE ENGINE [{}] REQUEST BY USER [{}]", id, logUser);

        Engine engine = repo.findById(id)
                .orElseThrow(() -> new EngineNotFoundException("Engine [%d] was not found.".formatted(id)));

        if (!engine.getName().equals(payload.name())) {
            if (repo.existsByName(payload.name())) {
                throw new EngineAlreadyExistsException("Engine [%s] already exists.".formatted(payload.name()));
            }
        }

        Engine mapped = mapper.update(engine, payload);

        repo.save(mapped);

        log.info("UPDATED ENGINE [{}] REQUEST BY USER [{}]", id, logUser);
    }

    @Override
    @Transactional
    public void delete(Integer id, OidcUser user) {
        String logUser = user == null ? "Anonymous" : user.getSubject();
        log.info("DELETE ENGINE [{}] REQUEST BY USER [{}]", id, logUser);

        Engine engine = repo.findById(id)
                .orElseThrow(() -> new EngineNotFoundException("Engine [%d] was not found.".formatted(id)));

        repo.delete(engine);

        log.info("DELETED ENGINE [{}] REQUEST BY USER [{}]", id, logUser);
    }

    @Override
    public PageDto<EngineDto> readAllAsPage(Pageable pageable, OidcUser user) {
        String logUser = user == null ? "Anonymous" : user.getSubject();
        log.info("READ ENGINES PAGE REQUEST BY USER [{}]", logUser);

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
