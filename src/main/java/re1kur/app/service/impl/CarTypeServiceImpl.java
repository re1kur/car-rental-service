package re1kur.app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re1kur.app.core.dto.CarTypeDto;
import re1kur.app.core.dto.PageDto;
import re1kur.app.core.exception.CarTypeAlreadyExistsException;
import re1kur.app.core.exception.CarTypeNotFoundException;
import re1kur.app.core.payload.CarTypePayload;
import re1kur.app.core.payload.CarTypeUpdatePayload;
import re1kur.app.entity.CarType;
import re1kur.app.mapper.CarTypeMapper;
import re1kur.app.repository.CarTypeRepository;
import re1kur.app.service.CarTypeService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarTypeServiceImpl implements CarTypeService {
    private final CarTypeRepository repo;
    private final CarTypeMapper mapper;

    @Override
    @Transactional
    public Integer create(CarTypePayload payload, OidcUser user) {
        String logUser = user == null ? "Anonymous" : user.getSubject();
        log.info("CREATE CAR TYPE REQUEST [{}] BY USER [{}]", payload, logUser);

        String name = payload.name();
        if (repo.existsByName(name))
            throw new CarTypeAlreadyExistsException("Car type with name '%s' already exists.".formatted(name));

        CarType mapped = mapper.create(payload);

        CarType saved = repo.save(mapped);
        Integer typeId = saved.getId();

        log.info("CREATE CAR TYPE REQUEST [{}] BY USER [{}]", typeId, logUser);
        return typeId;
    }

    @Override
    public CarTypeDto read(Integer id, OidcUser user) {
        String logUser = user == null ? "Anonymous" : user.getSubject();
        log.info("READ CAR TYPE [{}] REQUEST BY USER [{}]", id, logUser);

        CarType carType = repo.findById(id).orElseThrow(() ->
                new CarTypeNotFoundException("Car type with ID [%d] was not found.".formatted(id)));

        return mapper.read(carType);
    }

    @Override
    @Transactional
    public void update(CarTypeUpdatePayload payload, Integer id, OidcUser user) {
        String logUser = user == null ? "Anonymous" : user.getSubject();
        log.info("UPDATE CAR TYPE [{}] REQUEST BY USER [{}]", id, logUser);

        CarType carType = repo.findById(id).orElseThrow(() ->
                new CarTypeNotFoundException("Car type with ID [%d] was not found.".formatted(id)));

        String name = payload.name();
        if (name.equals(carType.getName()))
            if (repo.existsByName(name))
                throw new CarTypeAlreadyExistsException("Car type with name '%s' already exists.".formatted(name));

        mapper.update(carType, payload);

        repo.save(carType);

        log.info("UPDATED CAR TYPE [{}] REQUEST BY USER [{}]", id, logUser);
    }

    @Override
    @Transactional
    public void delete(Integer id, OidcUser user) {
        String logUser = user == null ? "Anonymous" : user.getSubject();
        log.info("DELETE CAR TYPE [{}] REQUEST BY USER [{}]", id, logUser);

        CarType carType = repo.findById(id).orElseThrow(() ->
                new CarTypeNotFoundException("Car type with ID [%d] was not found.".formatted(id)));

        repo.delete(carType);

        log.info("DELETED CAR TYPE [{}] REQUEST BY USER [{}]", id, logUser);
    }

    @Override
    public PageDto<CarTypeDto> readAllAsPage(Pageable pageable, OidcUser user) {
        String logUser = user == null ? "Anonymous" : user.getSubject();
        log.info("READ CAR TYPES PAGE REQUEST BY USER [{}]", logUser)
        ;
        return mapper.readPage(repo.findAll(pageable));
    }

    @Override
    public List<CarTypeDto> readAll() {
        List<CarType> found = (List<CarType>) repo.findAll();
        return found.stream().map(mapper::read).toList();
    }

    @Override
    public CarType get(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new CarTypeNotFoundException("Car type with ID [%d] was not found.".formatted(id)));
    }
}
