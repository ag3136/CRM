package crm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Service for managing file storage in Amazon S3.
 * Replaces local file system operations with cloud-native S3 storage.
 */
@Service
@Slf4j
public class S3StorageService {

    @Value("${aws.s3.bucket.name:crm-data-bucket}")
    private String bucketName;

    @Value("${aws.region:us-east-1}")
    private String awsRegion;

    private S3Client s3Client;

    @PostConstruct
    public void init() {
        try {
            // Initialize S3 client with default credentials provider
            // In AWS environment, this will use IAM roles automatically
            s3Client = S3Client.builder()
                    .region(Region.of(awsRegion))
                    .credentialsProvider(DefaultCredentialsProvider.create())
                    .build();
            log.info("S3 client initialized successfully for bucket: {} in region: {}", bucketName, awsRegion);
        } catch (Exception e) {
            log.error("Failed to initialize S3 client", e);
            throw new RuntimeException("Failed to initialize S3 storage service", e);
        }
    }

    @PreDestroy
    public void cleanup() {
        if (s3Client != null) {
            s3Client.close();
            log.info("S3 client closed");
        }
    }

    /**
     * Upload a file to S3 from byte array.
     *
     * @param key The S3 object key (file path in bucket)
     * @param content The file content as byte array
     * @param contentType The content type (e.g., "application/pdf")
     * @return The S3 object key of the uploaded file
     */
    public String uploadFile(String key, byte[] content, String contentType) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(contentType)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(content));
            log.info("Successfully uploaded file to S3: s3://{}/{}", bucketName, key);
            return key;
        } catch (S3Exception e) {
            log.error("Failed to upload file to S3: {}", key, e);
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    /**
     * Upload a file to S3 from input stream.
     *
     * @param key The S3 object key (file path in bucket)
     * @param inputStream The file content as input stream
     * @param contentLength The content length in bytes
     * @param contentType The content type (e.g., "application/pdf")
     * @return The S3 object key of the uploaded file
     */
    public String uploadFile(String key, InputStream inputStream, long contentLength, String contentType) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(contentType)
                    .contentLength(contentLength)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, contentLength));
            log.info("Successfully uploaded file to S3: s3://{}/{}", bucketName, key);
            return key;
        } catch (S3Exception e) {
            log.error("Failed to upload file to S3: {}", key, e);
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    /**
     * Get the S3 URI for a given key.
     *
     * @param key The S3 object key
     * @return The full S3 URI (s3://bucket/key)
     */
    public String getS3Uri(String key) {
        return String.format("s3://%s/%s", bucketName, key);
    }

    /**
     * Get the bucket name.
     *
     * @return The configured S3 bucket name
     */
    public String getBucketName() {
        return bucketName;
    }
}
