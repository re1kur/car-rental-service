package re1kur.fs.mapper.impl;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import re1kur.fs.dto.FileDto;
import re1kur.fs.dto.PresignedUrl;
import re1kur.fs.entity.File;
import re1kur.fs.mapper.FileMapper;

import java.time.Instant;
import java.time.OffsetDateTime;

@Component
public class FileMapperImpl implements FileMapper {
    @Override
    public File upload(MultipartFile payload, String id, PresignedUrl resp) {
        return File.builder()
                .id(id)
                .url(resp.url())
                .uploadedAt(Instant.now())
                .urlExpiresAt(resp.expiration())
                .mediaType(payload.getContentType())
                .build();
    }

    @Override
    public FileDto read(File saved) {
        return FileDto.builder()
                .id(saved.getId())
                .mediaType(saved.getMediaType())
                .url(saved.getUrl())
                .uploadedAt(saved.getUploadedAt())
                .urlExpiresAt(saved.getUrlExpiresAt())
                .build();
    }
}
