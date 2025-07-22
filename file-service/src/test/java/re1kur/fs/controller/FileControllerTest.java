package re1kur.fs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import re1kur.fs.dto.FileDto;
import re1kur.fs.exceptions.FileNotFoundException;
import re1kur.fs.service.FileService;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = FileController.class)
class FileControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private FileService service;

    private static final String URL = "/api/files";

    @Test
    void testUpload__ValidFile__DoesNotThrowException() throws Exception {
        byte[] content = new byte[0];
        MockMultipartFile file = new MockMultipartFile("file", content);

        FileDto expected = FileDto.builder()
                .id("SOME-ID")
                .mediaType("multipart/form-data")
                .uploadedAt(Instant.now())
                .urlExpiresAt(Instant.now().plus(Duration.ofDays(7)))
                .build();

        Mockito.when(service.upload(file)).thenReturn(FileDto.builder()
                .id("SOME-ID")
                .mediaType("multipart/form-data")
                .uploadedAt(expected.uploadedAt())
                .urlExpiresAt(expected.urlExpiresAt())
                .build());


        mvc.perform(MockMvcRequestBuilders
                        .multipart(URL + "/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
    }

    @Test
    void testUpload__NotReadingFile__ThrowIOException() throws Exception {
        byte[] content = new byte[0];
        MockMultipartFile file = new MockMultipartFile("file", content);

        Mockito.when(service.upload(file)).thenThrow(IOException.class);


        mvc.perform(MockMvcRequestBuilders
                        .multipart(URL + "/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetUrl__ValidFile__DoesNotThrowException() throws Exception {
        String id = "SOME-ID";
        String url = "SOME-URL";

        Mockito.when(service.getUrl("SOME-ID")).thenReturn("SOME-URL");

        mvc.perform(MockMvcRequestBuilders
                        .get(URL + "/get/" + id)
                )
                .andExpect(status().isFound())
                .andExpect(content().string(url));
    }

    @Test
    void testGetUrl__NotExistingFile__ReturnsNotFound() throws Exception {
        String id = "SOME-ID";

        Mockito.when(service.getUrl("SOME-ID")).thenThrow(FileNotFoundException.class);

        mvc.perform(MockMvcRequestBuilders
                        .get(URL + "/get/" + id)
                )
                .andExpect(status().isNotFound());
    }
}