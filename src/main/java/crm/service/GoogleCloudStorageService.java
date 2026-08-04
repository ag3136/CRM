package crm.service;

import com.google.cloud.storage.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Service for interacting with Google Cloud Storage.
 * Handles file upload, download, and deletion operations.
 */
@Service
@Slf4j
public class GoogleCloudStorageService {

    @Value("${gcs.bucket.name}")
    private String bucketName;

    @Value("${gcs.project.id:}")
    private String projectId;

    private Storage storage;

    @PostConstruct
    public void init() {
        try {
            if (projectId != null && !projectId.isEmpty()) {
                storage = StorageOptions.newBuilder()
                        .setProjectId(projectId)
                        .build()
                        .getService();
            } else {
                // Use default credentials (from GOOGLE_APPLICATION_CREDENTIALS env var)
                storage = StorageOptions.getDefaultInstance().getService();
            }
            log.info("Google Cloud Storage service initialized successfully for bucket: {}", bucketName);
        } catch (Exception e) {
            log.error("Failed to initialize Google Cloud Storage service", e);
            throw new RuntimeException("Failed to initialize GCS service", e);
        }
    }

    /**
     * Upload a file to Google Cloud Storage
     *
     * @param fileName    Name of the file to upload
     * @param content     File content as byte array
     * @param contentType MIME type of the file
     * @return The public URL of the uploaded file
     */
    public String uploadFile(String fileName, byte[] content, String contentType) {
        try {
            BlobId blobId = BlobId.of(bucketName, fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(contentType)
                    .build();

            Blob blob = storage.create(blobInfo, content);
            
            log.info("File uploaded successfully to GCS: {}", fileName);
            return String.format("gs://%s/%s", bucketName, fileName);
        } catch (Exception e) {
            log.error("Failed to upload file to GCS: {}", fileName, e);
            throw new RuntimeException("Failed to upload file to GCS", e);
        }
    }

    /**
     * Upload a file to Google Cloud Storage from an InputStream
     *
     * @param fileName    Name of the file to upload
     * @param inputStream InputStream containing file content
     * @param contentType MIME type of the file
     * @return The public URL of the uploaded file
     */
    public String uploadFile(String fileName, InputStream inputStream, String contentType) throws IOException {
        try {
            BlobId blobId = BlobId.of(bucketName, fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(contentType)
                    .build();

            // Read all bytes from input stream
            byte[] content = new byte[inputStream.available()];
            inputStream.read(content);

            Blob blob = storage.create(blobInfo, content);
            
            log.info("File uploaded successfully to GCS: {}", fileName);
            return String.format("gs://%s/%s", bucketName, fileName);
        } catch (Exception e) {
            log.error("Failed to upload file to GCS: {}", fileName, e);
            throw new RuntimeException("Failed to upload file to GCS", e);
        }
    }

    /**
     * Download a file from Google Cloud Storage
     *
     * @param fileName Name of the file to download
     * @return File content as byte array
     */
    public byte[] downloadFile(String fileName) {
        try {
            BlobId blobId = BlobId.of(bucketName, fileName);
            Blob blob = storage.get(blobId);
            
            if (blob == null) {
                log.warn("File not found in GCS: {}", fileName);
                return null;
            }
            
            log.info("File downloaded successfully from GCS: {}", fileName);
            return blob.getContent();
        } catch (Exception e) {
            log.error("Failed to download file from GCS: {}", fileName, e);
            throw new RuntimeException("Failed to download file from GCS", e);
        }
    }

    /**
     * Delete a file from Google Cloud Storage
     *
     * @param fileName Name of the file to delete
     * @return true if file was deleted, false otherwise
     */
    public boolean deleteFile(String fileName) {
        try {
            BlobId blobId = BlobId.of(bucketName, fileName);
            boolean deleted = storage.delete(blobId);
            
            if (deleted) {
                log.info("File deleted successfully from GCS: {}", fileName);
            } else {
                log.warn("File not found in GCS for deletion: {}", fileName);
            }
            
            return deleted;
        } catch (Exception e) {
            log.error("Failed to delete file from GCS: {}", fileName, e);
            throw new RuntimeException("Failed to delete file from GCS", e);
        }
    }

    /**
     * Check if a file exists in Google Cloud Storage
     *
     * @param fileName Name of the file to check
     * @return true if file exists, false otherwise
     */
    public boolean fileExists(String fileName) {
        try {
            BlobId blobId = BlobId.of(bucketName, fileName);
            Blob blob = storage.get(blobId);
            return blob != null && blob.exists();
        } catch (Exception e) {
            log.error("Failed to check file existence in GCS: {}", fileName, e);
            return false;
        }
    }

    /**
     * Get the public URL for a file in Google Cloud Storage
     *
     * @param fileName Name of the file
     * @return The GCS URL of the file
     */
    public String getFileUrl(String fileName) {
        return String.format("gs://%s/%s", bucketName, fileName);
    }
}
