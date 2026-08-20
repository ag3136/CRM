package crm.service;

import com.azure.core.util.BinaryData;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Azure Blob Storage Service for cloud-native file operations.
 * Replaces local file system operations with Azure Blob Storage for durability and scalability.
 */
@Service
@Slf4j
public class AzureBlobStorageService {

    @Value("${azure.storage.connection-string:}")
    private String connectionString;

    @Value("${azure.storage.container-name:crm-files}")
    private String containerName;

    @Value("${AZURE_STORAGE_ACCOUNT_NAME:}")
    private String storageAccountName;

    private BlobServiceClient blobServiceClient;
    private BlobContainerClient containerClient;

    @PostConstruct
    public void init() {
        try {
            // Try to use connection string first (for development/testing)
            if (connectionString != null && !connectionString.isEmpty()) {
                log.info("Initializing Azure Blob Storage with connection string");
                blobServiceClient = new BlobServiceClientBuilder()
                        .connectionString(connectionString)
                        .buildClient();
            } 
            // Use managed identity in production (recommended for Azure environments)
            else if (storageAccountName != null && !storageAccountName.isEmpty()) {
                log.info("Initializing Azure Blob Storage with managed identity for account: {}", storageAccountName);
                String endpoint = String.format("https://%s.blob.core.windows.net", storageAccountName);
                blobServiceClient = new BlobServiceClientBuilder()
                        .endpoint(endpoint)
                        .credential(new DefaultAzureCredentialBuilder().build())
                        .buildClient();
            } else {
                log.warn("Azure Blob Storage not configured. Set AZURE_STORAGE_CONNECTION_STRING or AZURE_STORAGE_ACCOUNT_NAME environment variable.");
                return;
            }

            // Create container if it doesn't exist
            containerClient = blobServiceClient.getBlobContainerClient(containerName);
            if (!containerClient.exists()) {
                containerClient.create();
                log.info("Created Azure Blob Storage container: {}", containerName);
            } else {
                log.info("Using existing Azure Blob Storage container: {}", containerName);
            }
        } catch (Exception e) {
            log.error("Failed to initialize Azure Blob Storage: {}", e.getMessage(), e);
            // In development, this might fail if Azure is not configured
            // Application can still run but file operations will fail
        }
    }

    /**
     * Upload a file to Azure Blob Storage
     * @param blobName The name of the blob (file) to create
     * @param data The file content as byte array
     * @return The URL of the uploaded blob
     */
    public String uploadFile(String blobName, byte[] data) {
        if (containerClient == null) {
            throw new IllegalStateException("Azure Blob Storage is not initialized. Check configuration.");
        }

        try {
            BlobClient blobClient = containerClient.getBlobClient(blobName);
            blobClient.upload(BinaryData.fromBytes(data), true);
            log.info("Successfully uploaded file to Azure Blob Storage: {}", blobName);
            return blobClient.getBlobUrl();
        } catch (Exception e) {
            log.error("Failed to upload file to Azure Blob Storage: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload file to Azure Blob Storage", e);
        }
    }

    /**
     * Upload a file to Azure Blob Storage from InputStream
     * @param blobName The name of the blob (file) to create
     * @param inputStream The input stream containing file content
     * @param length The length of the content
     * @return The URL of the uploaded blob
     */
    public String uploadFile(String blobName, InputStream inputStream, long length) {
        if (containerClient == null) {
            throw new IllegalStateException("Azure Blob Storage is not initialized. Check configuration.");
        }

        try {
            BlobClient blobClient = containerClient.getBlobClient(blobName);
            blobClient.upload(inputStream, length, true);
            log.info("Successfully uploaded file to Azure Blob Storage: {}", blobName);
            return blobClient.getBlobUrl();
        } catch (Exception e) {
            log.error("Failed to upload file to Azure Blob Storage: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload file to Azure Blob Storage", e);
        }
    }

    /**
     * Download a file from Azure Blob Storage
     * @param blobName The name of the blob (file) to download
     * @return The file content as byte array
     */
    public byte[] downloadFile(String blobName) {
        if (containerClient == null) {
            throw new IllegalStateException("Azure Blob Storage is not initialized. Check configuration.");
        }

        try {
            BlobClient blobClient = containerClient.getBlobClient(blobName);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            blobClient.downloadStream(outputStream);
            log.info("Successfully downloaded file from Azure Blob Storage: {}", blobName);
            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("Failed to download file from Azure Blob Storage: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to download file from Azure Blob Storage", e);
        }
    }

    /**
     * Delete a file from Azure Blob Storage
     * @param blobName The name of the blob (file) to delete
     */
    public void deleteFile(String blobName) {
        if (containerClient == null) {
            throw new IllegalStateException("Azure Blob Storage is not initialized. Check configuration.");
        }

        try {
            BlobClient blobClient = containerClient.getBlobClient(blobName);
            blobClient.delete();
            log.info("Successfully deleted file from Azure Blob Storage: {}", blobName);
        } catch (Exception e) {
            log.error("Failed to delete file from Azure Blob Storage: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete file from Azure Blob Storage", e);
        }
    }

    /**
     * Check if a file exists in Azure Blob Storage
     * @param blobName The name of the blob (file) to check
     * @return true if the file exists, false otherwise
     */
    public boolean fileExists(String blobName) {
        if (containerClient == null) {
            return false;
        }

        try {
            BlobClient blobClient = containerClient.getBlobClient(blobName);
            return blobClient.exists();
        } catch (Exception e) {
            log.error("Failed to check file existence in Azure Blob Storage: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Get the URL of a blob in Azure Blob Storage
     * @param blobName The name of the blob
     * @return The URL of the blob
     */
    public String getBlobUrl(String blobName) {
        if (containerClient == null) {
            throw new IllegalStateException("Azure Blob Storage is not initialized. Check configuration.");
        }

        BlobClient blobClient = containerClient.getBlobClient(blobName);
        return blobClient.getBlobUrl();
    }
}
