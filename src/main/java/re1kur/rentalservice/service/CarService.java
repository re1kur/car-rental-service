package re1kur.rentalservice.service;

import re1kur.rentalservice.dto.car.CarReadDto;

import java.util.List;

public interface CarService {

    List<CarReadDto> findAll();
}
