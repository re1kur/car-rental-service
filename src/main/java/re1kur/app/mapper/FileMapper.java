package re1kur.app.mapper;

import org.springframework.web.multipart.MultipartFile;
import re1kur.app.core.dto.FileDto;
import re1kur.app.core.other.PresignedUrl;
import re1kur.app.entity.File;

public interface FileMapper {
    File upload(MultipartFile payload, String id, PresignedUrl url);

    FileDto read(File saved);
}
