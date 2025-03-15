package re1kur.rentalservice.service;

import re1kur.rentalservice.dto.make.MakeReadDto;

import java.util.List;

public interface MakeService {

    List<MakeReadDto> findAll();
}
