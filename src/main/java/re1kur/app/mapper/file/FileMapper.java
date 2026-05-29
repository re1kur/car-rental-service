package re1kur.app.mapper.file;

import org.springframework.web.multipart.MultipartFile;
import re1kur.app.dto.view.FileView;
import re1kur.app.entity.File;

public interface FileMapper {
    File upload(MultipartFile payload, String id);

    FileView read(File saved);
}
