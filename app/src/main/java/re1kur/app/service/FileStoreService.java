package re1kur.app.service;

import org.springframework.web.multipart.MultipartFile;
import re1kur.app.core.car.images.CarImageWriteDto;
import re1kur.app.entity.Image;

import java.io.IOException;

public interface FileStoreService {
    CarImageWriteDto uploadCarImage(CarImageWriteDto image) throws IOException;

    String uploadMakeImage(MultipartFile file) throws IOException;


    Image uploadImage(MultipartFile titleImg);

    void deleteImage(String id);
}
