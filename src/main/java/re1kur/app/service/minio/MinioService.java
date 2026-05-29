package re1kur.app.service.minio;

import org.springframework.web.multipart.MultipartFile;
import re1kur.app.entity.File;

import java.util.List;

public interface MinioService {
    File upload(MultipartFile payload);

    List<File> uploadAll(MultipartFile[] payloads);
}
