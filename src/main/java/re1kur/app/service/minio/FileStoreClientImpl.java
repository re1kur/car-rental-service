package re1kur.app.service.minio;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import re1kur.app.exception.MinioClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileStoreClientImpl implements FileStoreClient {
    private final S3Client s3Client;

    @Value("${minio.default-bucket}")
    private String bucket;

    @PostConstruct
    public void init() {
        ensureBucket();
        makeBucketPublic();
    }

    private void ensureBucket() {
        try {
            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucket).build());
            log.info("Bucket '{}' already exists", bucket);
        } catch (S3Exception e) {
            if (e instanceof NoSuchBucketException || e.statusCode() == 404) {
                CreateBucketResponse response = s3Client.createBucket(
                        CreateBucketRequest.builder().bucket(bucket).build());
                log.info("Created bucket: {}", response.location());
            } else {
                log.error("Error checking/creating bucket: {}", e.awsErrorDetails().errorMessage(), e);
                throw e;
            }
        }
    }

    private void makeBucketPublic() {
        String policy = """
                {
                  "Version": "2012-10-17",
                  "Statement": [
                    {
                      "Effect": "Allow",
                      "Principal": "*",
                      "Action": ["s3:GetObject"],
                      "Resource": ["arn:aws:s3:::%s/*"]
                    }
                  ]
                }""".formatted(bucket);

        s3Client.putBucketPolicy(PutBucketPolicyRequest.builder()
                .bucket(bucket)
                .policy(policy)
                .build());
        log.info("Bucket '{}' is now public for read", bucket);
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
}
