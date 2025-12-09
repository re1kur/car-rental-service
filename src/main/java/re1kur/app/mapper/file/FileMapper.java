package re1kur.app.mapper.file;

import org.springframework.web.multipart.MultipartFile;
import re1kur.app.model.dto.FileDto;
import re1kur.app.model.dto.PresignedUrl;
import re1kur.app.model.entity.File;

public interface FileMapper {
    File upload(MultipartFile payload, String id, PresignedUrl url);

    FileDto read(File saved);
}
