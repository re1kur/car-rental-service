package re1kur.rentalservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import re1kur.rentalservice.dto.car.CarReadDto;
import re1kur.rentalservice.mapper.CarMapper;
import re1kur.rentalservice.repository.CarRepository;
import re1kur.rentalservice.service.CarService;

import java.util.List;

@Service
public class DefaultCarService implements CarService {
    private final CarRepository repo;
    private final CarMapper mapper;

    @Autowired
    public DefaultCarService(CarRepository repo, CarMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }


    @Override
    public List<CarReadDto> findAll() {
        return repo.findAll().stream().map(car -> mapper.read(car, false, false)).toList();
    }
}
