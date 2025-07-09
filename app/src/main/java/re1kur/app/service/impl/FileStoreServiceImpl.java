package re1kur.app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import re1kur.app.core.dto.FileDto;
import re1kur.app.core.car.images.CarImageWriteDto;
import re1kur.app.core.exception.FIleUploadException;
import re1kur.app.entity.Image;
import re1kur.app.mapper.ImageMapper;
import re1kur.app.repository.ImageRepository;
import re1kur.app.service.FileStoreService;

import java.io.*;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStoreServiceImpl implements FileStoreService {
    private final WebClient fileStoreClient;
    private final ImageMapper mapper;
    private final ImageRepository repo;

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
//            imageDto.setUrl(url);
//            imageDto.setExpiresAt(response.urlExpiresAt();
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

    @Override
    public Image uploadImage(MultipartFile titleImg) {
        if (titleImg.isEmpty())
            return null;

        log.info("Send request to upload image: {}", titleImg);
        MultipartBodyBuilder builder = createMultipartRequestBody(titleImg);
        FileDto response = fileStoreClient.post()
                .uri("/upload")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(FileDto.class)
                .block();

        if (response == null)
            throw new FIleUploadException("File upload response received, but response body is null.");
        log.info("File service uploaded image.");

        Image img = mapper.write(response);
        repo.save(img);

        return img;
    }

    @Override
    public void deleteImage(String id) {
        log.info("DELETE IMAGE REQUEST: IMAGE WITH ID [{}]", id);
    }
}
