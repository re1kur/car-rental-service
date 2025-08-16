package re1kur.app.util.minio;

import org.springframework.web.multipart.MultipartFile;
import re1kur.app.core.other.PresignedUrl;

public interface FileStoreClient {
    void upload(String id, MultipartFile payload);

    PresignedUrl getUrl(String id);
}
