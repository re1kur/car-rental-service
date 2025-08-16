package re1kur.app.service;

import org.springframework.web.multipart.MultipartFile;
import re1kur.app.entity.File;

import java.util.List;

public interface MinioService {
    File upload(MultipartFile payload);

    String getUrl(String id);

    List<File> uploadAll(MultipartFile[] payloads);
}
