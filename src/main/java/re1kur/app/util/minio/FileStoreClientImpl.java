package re1kur.app.util.minio;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import re1kur.app.core.exception.MinioClientException;
import re1kur.app.core.other.PresignedUrl;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileStoreClientImpl implements FileStoreClient {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${minio.default-bucket}")
    private String bucket;

    @Value("${minio.url.time-to-live}")
    private Integer ttl;

    @PostConstruct
    public void init() {
        try {
            HeadBucketRequest headBucketRequest = HeadBucketRequest.builder()
                    .bucket(bucket)
                    .build();
            s3Client.headBucket(headBucketRequest);
            log.info("Bucket '{}' already exists", bucket);
        } catch (S3Exception e) {
            if (e instanceof NoSuchBucketException || e.statusCode() == 404) {
                CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                        .bucket(bucket)
                        .build();
                CreateBucketResponse response = s3Client.createBucket(createBucketRequest);
                log.info("Created bucket: {}", response.location());
            } else {
                log.error("Error checking/creating bucket: {}", e.awsErrorDetails().errorMessage(), e);
                throw e;
            }
        }
    }


    @Override
    public void upload(String id, MultipartFile payload) {
        log.info("Uploading file request: {} | {}", payload.getOriginalFilename(), payload.getContentType());
        try (InputStream inputStream = payload.getInputStream()) {
            PutObjectRequest request = PutObjectRequest.builder()
                    .key(id)
                    .bucket(bucket)
                    .contentType(payload.getContentType())
                    .build();
            RequestBody requestBody = RequestBody.fromInputStream(inputStream, payload.getSize());
            PutObjectResponse putObjectResponse = s3Client.putObject(request, requestBody);
            log.info("Uploaded file : {}", putObjectResponse.toString());
        } catch (IOException e) {
            log.error("Error reading/uploading file: {}", e.getMessage(), e);
            throw new MinioClientException("Error executing reading/uploading file.");
        }
    }

    @Override
    public PresignedUrl getUrl(String id) {
        log.info("Getting presigned url for file {}", id);
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(id)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofDays(ttl))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(presignRequest);

        log.info("Received presigned URL: Host: {} | Port : {} | Path : {}",
                presignedGetObjectRequest.url().getHost(),
                presignedGetObjectRequest.url().getPort(),
                presignedGetObjectRequest.url().getPath());

        return new PresignedUrl(presignedGetObjectRequest.url().toString(), presignedGetObjectRequest.expiration());
    }
}
