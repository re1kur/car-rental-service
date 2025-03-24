package re1kur.rentalservice.service;

import re1kur.rentalservice.dto.car.images.CarImageWriteDto;

import java.io.IOException;

public interface FileStoreService {
    CarImageWriteDto upload(CarImageWriteDto image) throws IOException;

}
