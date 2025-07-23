package re1kur.fs.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import re1kur.fs.dto.FileDto;
import re1kur.fs.service.FileService;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController {
    private final FileService service;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileDto> uploadFile(@RequestPart("file") MultipartFile payload) throws IOException {
        log.info("UPLOAD endpoint called. File name: {}, size: {}", payload.getOriginalFilename(), payload.getSize());

        FileDto body = service.upload(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<String> downloadFile(@PathVariable("id") String id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(service.getUrl(id));
    }
}
