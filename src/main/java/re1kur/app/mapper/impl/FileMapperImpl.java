package re1kur.app.mapper.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import re1kur.app.core.dto.FileDto;
import re1kur.app.entity.File;
import re1kur.app.mapper.FileMapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class FileMapperImpl implements FileMapper {

    @Value("${minio.public-url}")
    private String publicUrl;

    @Value("${minio.default-bucket}")
    private String bucket;

    @Override
    public File upload(MultipartFile payload, String id) {
        return File.builder()
                .id(id)
                .uploadedAt(Instant.now())
                .mediaType(payload.getContentType())
                .build();
    }

    @Override
    public FileDto read(File file) {
        if (file == null)
            return null;
        Instant uploadedAt = file.getUploadedAt();
        return FileDto.builder()
                .id(file.getId())
                .mediaType(file.getMediaType())
                .url("%s/%s/%s".formatted(publicUrl, bucket, file.getId()))
                .uploadedAt(uploadedAt != null ? LocalDateTime.ofInstant(uploadedAt, ZoneId.systemDefault()) : null)
                .build();
    }
}
