package re1kur.fs.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import re1kur.fs.client.StoreClient;
import re1kur.fs.dto.FileDto;
import re1kur.fs.dto.PresignedUrl;
import re1kur.fs.entity.File;
import re1kur.fs.exceptions.FileNotFoundException;
import re1kur.fs.exceptions.UrlUpdateException;
import re1kur.fs.mapper.FileMapper;
import re1kur.fs.repository.FileRepository;
import re1kur.fs.service.impl.FileServiceImpl;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {
    @InjectMocks
    private FileServiceImpl service;

    @Mock
    private FileRepository repo;

    @Mock
    private FileMapper mapper;

    @Mock
    private StoreClient client;

    @Test
    void testUpload__ValidFile__DoesNotThrowException() {
        MultipartFile payload = new MockMultipartFile("file", "test", "text/plain", "test".getBytes());
        Instant mockExp = Instant.now();
        PresignedUrl url = new PresignedUrl("url", mockExp);
        File file = File.builder()
                .url("url")
                .build();
        FileDto expected = FileDto.builder()
                .url("url")
                .build();

        Mockito.when(repo.existsById(Mockito.anyString())).thenReturn(false);
        Mockito.when(client.getUrl(Mockito.anyString())).thenReturn(url);
        Mockito.when(mapper.upload(Mockito.any(MultipartFile.class), Mockito.anyString(), Mockito.any(PresignedUrl.class)))
                .thenReturn(file);
        Mockito.when(repo.save(Mockito.any(File.class))).thenReturn(file);
        Mockito.when(mapper.read(file)).thenReturn(expected);

        FileDto result = Assertions.assertDoesNotThrow(() -> service.upload(payload));

        Assertions.assertEquals(expected, result);

        Mockito.verify(repo, Mockito.times(1)).save(Mockito.any(File.class));
        Mockito.verify(repo, Mockito.times(1)).existsById(Mockito.anyString());
    }

    @Test
    void testUpload__NotReadingFile__ThrowsIOException() throws IOException {
        MultipartFile payload = new MockMultipartFile("file", "test", "text/plain", "test".getBytes());

        Mockito.when(repo.existsById(Mockito.anyString())).thenReturn(false);
        Mockito.doThrow(IOException.class).when(client).upload(Mockito.anyString(), Mockito.any(MultipartFile.class));

        Assertions.assertThrows(IOException.class, () -> service.upload(payload));

        Mockito.verify(repo, Mockito.times(1)).existsById(Mockito.any(String.class));
        Mockito.verifyNoMoreInteractions(repo);
        Mockito.verifyNoInteractions(mapper);
    }

    @Test
    void getUrl__ValidFile__DoesNotThrowException() {
        String id = "some-file-id";
        String currentUrl = "http://some-url";
        Instant notExpired = Instant.now().plusSeconds(60);

        File file = File.builder()
                .id(id)
                .url(currentUrl)
                .urlExpiresAt(notExpired)
                .build();

        Mockito.when(repo.findById(id)).thenReturn(Optional.of(file));

        String result = service.getUrl(id);

        Assertions.assertEquals(currentUrl, result);
        Mockito.verify(repo, Mockito.never()).save(Mockito.any());
    }

    @Test
    void getUrl__ExpiredUrlFile__UpdatesUrlAndReturnsNewOne() {
        String id = "expired-file-id";
        String oldUrl = "http://old-url";
        String newUrl = "http://new-url";
        Instant expiredAt = Instant.now().minusSeconds(60);
        Instant newExpiration = Instant.now().plusSeconds(3600);

        File file = File.builder()
                .id(id)
                .url(oldUrl)
                .urlExpiresAt(expiredAt)
                .build();

        PresignedUrl presignedUrl = new PresignedUrl(newUrl, newExpiration);
        File updatedFile = File.builder()
                .id(id)
                .url(newUrl)
                .urlExpiresAt(newExpiration)
                .build();

        Mockito.when(repo.findById(id)).thenReturn(Optional.of(file));
        Mockito.when(client.getUrl(id)).thenReturn(presignedUrl);
        Mockito.when(repo.save(Mockito.any(File.class))).thenReturn(updatedFile);

        String result = service.getUrl(id);

        Assertions.assertEquals(newUrl, result);
        Mockito.verify(repo, Mockito.times(1)).save(Mockito.any(File.class));
    }

    @Test
    void getUrl__FileNotFound__ThrowsFileNotFoundException() {
        String id = "nonexistent-id";

        Mockito.when(repo.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(FileNotFoundException.class, () -> service.getUrl(id));
    }

    @Test
    void getUrl__UpdateFailsWithSameExpiration__ThrowsUrlUpdateException() {
        String id = "stale-url-id";
        String currentUrl = "http://old-url";
        Instant expiredAt = Instant.now().minusSeconds(60);

        File file = File.builder()
                .id(id)
                .url(currentUrl)
                .urlExpiresAt(expiredAt)
                .build();

        PresignedUrl presignedUrl = new PresignedUrl("http://still-old-url", expiredAt);

        Mockito.when(repo.findById(id)).thenReturn(Optional.of(file));
        Mockito.when(client.getUrl(id)).thenReturn(presignedUrl);

        Assertions.assertThrows(UrlUpdateException.class, () -> service.getUrl(id));

        Mockito.verify(repo, Mockito.never()).save(Mockito.any());
    }
}