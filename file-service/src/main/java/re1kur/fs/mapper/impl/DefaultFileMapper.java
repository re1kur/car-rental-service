package re1kur.fs.mapper.impl;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import re1kur.fs.dto.FileDto;
import re1kur.fs.dto.PresignedUrl;
import re1kur.fs.entity.File;
import re1kur.fs.mapper.FileMapper;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class DefaultFileMapper implements FileMapper {
    @Override
    public File upload(MultipartFile payload, String id, PresignedUrl resp) {
        return File.builder()
                .id(id)
                .url(resp.url())
                .uploadedAt(ZonedDateTime.now())
                .urlExpiresAt(resp.expiration().atZone(ZoneId.systemDefault()))
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
