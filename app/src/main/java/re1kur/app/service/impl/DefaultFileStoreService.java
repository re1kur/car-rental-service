package re1kur.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import re1kur.app.core.dto.FileDto;
import re1kur.app.core.car.images.CarImageWriteDto;
import re1kur.app.service.FileStoreService;

import java.io.*;

@Slf4j
@Service
public class DefaultFileStoreService implements FileStoreService {
    private final WebClient fileStoreClient;

    @Value("${custom.buckets.car-image}")
    private String carsBucket;

    @Value("${custom.buckets.make-image}")
    private String makesBucket;

    @Autowired
    public DefaultFileStoreService(WebClient fileStoreClient) {
        this.fileStoreClient = fileStoreClient;
    }

    private MultipartBodyBuilder createMultipartRequestBody(MultipartFile file) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", file.getResource());
        return builder;
    }

    @Override
    public CarImageWriteDto uploadCarImage(CarImageWriteDto imageDto) throws IOException {
        MultipartBodyBuilder builder = createMultipartRequestBody(imageDto.getImage());
        FileDto response = fileStoreClient.post()
                .uri("/upload")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(FileDto.class)
                .block();
        assert response != null;
        if (response.url() != null) {
            String url = response.url();
            imageDto.setUrl(url);
            imageDto.setExpiresAt(response.urlExpiresAt().toLocalDateTime());
            log.info("Successfully uploaded file: {}.", url);
        } else {
            log.error("Failed to get URL after upload.");
            throw new IOException("Could not upload file");
        }
        return imageDto;
    }

    @Override
    public String uploadMakeImage(MultipartFile file) throws IOException {
        MultipartBodyBuilder builder = createMultipartRequestBody(file);
        FileDto response = fileStoreClient.post()
                .uri("/upload")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(FileDto.class)
                .block();
        assert response != null;
        if (response.url() != null) {
            String url = response.url();
            log.info("Successfully uploaded file: {}.", url);
            return url;
        } else {
            log.error("Failed to get URL after upload.");
            throw new IOException("Could not upload file");
        }
    }
}
