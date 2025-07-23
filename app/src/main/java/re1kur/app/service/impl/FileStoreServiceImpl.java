package re1kur.app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import re1kur.app.core.dto.FileDto;
import re1kur.app.core.exception.FIleUploadException;
import re1kur.app.entity.image.Image;
import re1kur.app.mapper.ImageMapper;
import re1kur.app.repository.ImageRepository;
import re1kur.app.service.FileStoreService;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStoreServiceImpl implements FileStoreService {
    private final WebClient fileStoreClient;
    private final ImageMapper mapper;
    private final ImageRepository repo;

    @Override
    public Image uploadImage(MultipartFile payloadImage) {
        if (payloadImage == null || payloadImage.isEmpty()) return null;

        log.info("UPLOAD IMAGE [{}], SIZE [{}]", payloadImage.getOriginalFilename(), payloadImage.getSize());

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", payloadImage.getResource());

        FileDto response =
                fileStoreClient
                .post()
                .uri("/upload")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(FileDto.class)
                .block();

        if (response == null) {
            log.warn("UPLOAD FAILED: RESPONSE IS NULL.");
            throw new FIleUploadException("Upload failed: no response.");
        }

        log.info("Image uploaded successfully.");
        Image img = mapper.write(response);
        repo.save(img);
        return img;
    }

    @Override
    public void deleteImage(String id) {
        log.info("DELETE IMAGE [{}]", id);
    }

    @Override
    public List<Image> uploadImages(MultipartFile[] files) {
        return Arrays.stream(files)
                .map(this::uploadImage)
                .toList();
    }
}
