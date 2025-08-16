package re1kur.app.mapper.impl;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import re1kur.app.core.dto.FileDto;
import re1kur.app.core.other.PresignedUrl;
import re1kur.app.entity.File;
import re1kur.app.mapper.FileMapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

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
    public FileDto read(File file) {
        if (file == null)
            return null;
        return FileDto.builder()
                .id(file.getId())
                .mediaType(file.getMediaType())
                .url(file.getUrl())
                .uploadedAt(LocalDateTime.ofInstant(file.getUploadedAt(), ZoneId.systemDefault()))
                .urlExpiresAt(LocalDateTime.ofInstant(file.getUrlExpiresAt(), ZoneId.systemDefault()))
                .build();
    }
}
