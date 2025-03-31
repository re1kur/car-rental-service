package re1kur.rentalservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import re1kur.rentalservice.dto.car.images.CarImageWriteDto;
import re1kur.rentalservice.service.FileStoreService;

import java.io.*;
import java.time.LocalDateTime;
import java.util.UUID;

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

    @Override
    public CarImageWriteDto uploadCarImage(CarImageWriteDto imageDto) throws IOException {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        String uuid = UUID.randomUUID().toString();
        imageDto.setUrl(uuid);
        imageDto.setBucket(carsBucket);

        MultipartFile imageFile = imageDto.getImage();
        String contentType = imageFile.getContentType();
        byte[] imageBytes = imageFile.getBytes();

        log.info("Uploading file: name={}, size={}, contentType={}", uuid, imageBytes.length, contentType);

        ByteArrayResource array = new ByteArrayResource(imageBytes) {
            @Override
            public String getFilename() {
                return uuid;
            }
        };
        body.add("file", array);

        body.add("bucket", imageDto.getBucket());
        body.add("name", uuid);
        body.add("contentType", contentType);

        String url = fileStoreClient.post()
                .uri("/upload")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        if (url != null) {
            imageDto.setUrl(url);
            imageDto.setExpiresAt(LocalDateTime.now().plusDays(29));
            log.info("Successfully uploaded file: {}. URL: {}", uuid, url);
        } else {
            log.error("Failed to get URL after upload: {}", uuid);
            throw new IOException("Could not upload file");
        }
        return imageDto;
    }

    @Override
    public String uploadMakeImage(MultipartFile file) throws IOException {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        String uuid = UUID.randomUUID().toString();
        String contentType = file.getContentType();
        byte[] imageBytes = file.getBytes();

        log.info("Uploading file: name={}, size={}, contentType={}", uuid, imageBytes.length, contentType);

        ByteArrayResource array = new ByteArrayResource(imageBytes) {
            @Override
            public String getFilename() {
                return uuid;
            }
        };
        body.add("file", array);

        body.add("bucket", makesBucket);
        body.add("name", uuid);
        body.add("contentType", contentType);

        String url = fileStoreClient.post()
                .uri("/upload")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        if (url != null) {
            log.info("Successfully uploaded file: {}. URL: {}", uuid, url);
            return url;
        } else {
            log.error("Failed to get URL after upload: {}", uuid);
            throw new IOException("Could not upload file");
        }
    }
}
