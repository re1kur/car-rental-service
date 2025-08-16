package re1kur.app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import re1kur.app.core.exception.FileNotFoundException;
import re1kur.app.core.exception.UrlUpdateException;
import re1kur.app.core.other.PresignedUrl;
import re1kur.app.entity.File;
import re1kur.app.mapper.FileMapper;
import re1kur.app.repository.FileRepository;
import re1kur.app.service.MinioService;
import re1kur.app.util.minio.FileStoreClient;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {
    private final FileRepository repo;
    private final FileMapper mapper;
    private final FileStoreClient client;

    @Override
    @Transactional
    public File upload(MultipartFile payload) {
        if (payload == null || payload.isEmpty()) return null;

        String originalName = payload.getOriginalFilename();
        log.info("UPLOAD FILE [{}], SIZE [{}]", originalName, payload.getSize());

        String extension = "";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }

        String key;
        do {
            UUID id = UUID.randomUUID();
            key = id + extension;
        } while (repo.existsById(key));

        client.upload(key, payload);
        PresignedUrl resp = client.getUrl(key);

        File file = mapper.upload(payload, key, resp);

        file = repo.save(file);

        log.info("FILE UPLOADED SUCCESSFULLY [{}]", file.getId());
        return file;
    }


    @Override
    public String getUrl(String id) {
        log.info("GET URL OF FILE [{}]", id);
        File file = repo.findById(id).orElseThrow(() -> new FileNotFoundException("File with id '%s' does not exist.".formatted(id)));
        if (Instant.now().isAfter(file.getUrlExpiresAt())) {
            file = updateUrl(file);
        }
        return file.getUrl();
    }

    @Override
    public List<File> uploadAll(MultipartFile[] payloads) {
        if (payloads != null && payloads.length > 0) {
            return Arrays.stream(payloads)
                    .map(this::upload)
                    .toList();
        }
        return List.of();
    }

    private File updateUrl(File file) {
        PresignedUrl resp = client.getUrl(file.getId());
        if (resp.expiration().equals(file.getUrlExpiresAt())) {
            throw new UrlUpdateException("The expiration has not been updated.");
        }
        file.setUrl(resp.url());
        file.setUrlExpiresAt(resp.expiration());
        return repo.save(file);
    }
}
