package re1kur.app.service;

import org.springframework.web.multipart.MultipartFile;
import re1kur.app.dto.car.images.CarImageWriteDto;

import java.io.IOException;

public interface FileStoreService {
    CarImageWriteDto uploadCarImage(CarImageWriteDto image) throws IOException;

    String uploadMakeImage(MultipartFile file) throws IOException;



}
