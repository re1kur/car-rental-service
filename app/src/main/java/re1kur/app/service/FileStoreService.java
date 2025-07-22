package re1kur.app.service;

import org.springframework.web.multipart.MultipartFile;
import re1kur.app.core.car.images.CarImageWriteDto;
import re1kur.app.entity.image.Image;

import java.io.IOException;
import java.util.List;

public interface FileStoreService {
    Image uploadImage(MultipartFile titleImg);

    void deleteImage(String id);

    List<Image> uploadImages(MultipartFile[] files);
}
