package re1kur.app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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
    public void create(CarTypePayload payload) {
        log.info("Create CarType request: {}", payload.toString());

        String name = payload.name();
        if (repo.existsByName(name))
            throw new CarTypeAlreadyExistsException("Car type with name '%s' already exists.".formatted(name));

        CarType mapped = mapper.create(payload);

        repo.save(mapped);

        log.info("Created car type: {}", payload);
    }

    @Override
    public CarTypeDto read(Integer id) {
        CarType carType = repo.findById(id).orElseThrow(() ->
                new CarTypeNotFoundException("Car type with ID [%d] was not found.".formatted(id)));

        return mapper.read(carType);
    }

    @Override
    public void update(CarTypeUpdatePayload payload, Integer id) {
        log.info("Update CarType request: {}", payload.toString());

        CarType carType = repo.findById(id).orElseThrow(() ->
                new CarTypeNotFoundException("Car type with ID [%d] was not found.".formatted(id)));

        String name = payload.name();
        if (name.equals(carType.getName()))
            if (repo.existsByName(name))
                throw new CarTypeAlreadyExistsException("Car type with name '%s' already exists.".formatted(name));

        mapper.update(carType, payload);

        repo.save(carType);

        log.info("Updated car type: {}", carType);
    }

    @Override
    public void delete(Integer id) {
        log.info("Delete CarType with ID [{}] request.", id);

        CarType carType = repo.findById(id).orElseThrow(() ->
                new CarTypeNotFoundException("Car type with ID [%d] was not found.".formatted(id)));

        repo.delete(carType);

        log.info("Deleted car type with ID [{}]", id);
    }

    @Override
    public PageDto<CarTypeDto> readPage(Pageable pageable) {
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
