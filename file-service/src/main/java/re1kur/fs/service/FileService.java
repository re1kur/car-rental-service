package re1kur.fs.service;

import org.springframework.web.multipart.MultipartFile;
import re1kur.fs.dto.FileDto;

import java.io.IOException;

public interface FileService {
    FileDto upload(MultipartFile payload) throws IOException;

    String getUrl(String id);
}
