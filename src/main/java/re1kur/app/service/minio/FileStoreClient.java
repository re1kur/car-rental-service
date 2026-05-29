package re1kur.app.service.minio;

import org.springframework.web.multipart.MultipartFile;

public interface FileStoreClient {
    void upload(String id, MultipartFile payload);
}
