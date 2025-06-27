package re1kur.fs.mapper;

import org.springframework.web.multipart.MultipartFile;
import re1kur.fs.dto.FileDto;
import re1kur.fs.dto.PresignedUrl;
import re1kur.fs.entity.File;

public interface FileMapper {
    File upload(MultipartFile payload, String id, PresignedUrl url);

    FileDto read(File saved);
}
