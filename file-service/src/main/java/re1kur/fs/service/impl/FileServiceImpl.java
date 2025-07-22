package re1kur.fs.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import re1kur.fs.client.StoreClient;
import re1kur.fs.dto.FileDto;
import re1kur.fs.dto.PresignedUrl;
import re1kur.fs.entity.File;
import re1kur.fs.exceptions.FileNotFoundException;
import re1kur.fs.exceptions.UrlUpdateException;
import re1kur.fs.mapper.FileMapper;
import re1kur.fs.repository.FileRepository;
import re1kur.fs.service.FileService;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileRepository repo;
    private final FileMapper mapper;
    private final StoreClient client;

    @Override
    @Transactional
    public FileDto upload(MultipartFile payload) throws IOException {
        log.info("UPLOAD FILE [{}]", payload.getOriginalFilename());
        String originalFilename = payload.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
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
        log.info("FILE UPLOADED [{}]", file.getId());
        return mapper.read(file);
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
