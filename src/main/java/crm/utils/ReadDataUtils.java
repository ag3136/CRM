package crm.utils;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.identity.DefaultAzureCredentialBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * Cloud-native utility for reading files from Azure Blob Storage.
 * Replaces local file system dependencies with Azure Blob Storage operations.
 */
@Component
public class ReadDataUtils {

    private static String connectionString;
    private static String containerName;
    private static boolean useConnectionString;

    /**
     * Sets the Azure Blob Storage connection string from application properties.
     * This should be configured as an environment variable in cloud deployments.
     */
    @Value("${azure.storage.connection-string:#{null}}")
    public void setConnectionString(String connString) {
        connectionString = connString;
        useConnectionString = (connString != null && !connString.isEmpty());
    }

    /**
     * Sets the Azure Blob Storage container name from application properties.
     */
    @Value("${azure.storage.container-name:crm-files}")
    public void setContainerName(String container) {
        containerName = container;
    }

    /**
     * Downloads a file from Azure Blob Storage and returns it as a File object.
     * This method replaces the GUI-based file chooser with cloud storage access.
     *
     * @param blobName The name of the blob (file) to download from Azure Blob Storage
     * @param fileExtension Expected file extension for validation (e.g., "csv")
     * @return File object containing the downloaded content, or null if download fails
     */
    public static File ReadFile(String blobName, String fileExtension) {
        try {
            BlobServiceClient blobServiceClient = createBlobServiceClient();
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            BlobClient blobClient = containerClient.getBlobClient(blobName);

            // Validate file extension if provided
            if (fileExtension != null && !blobName.toLowerCase().endsWith("." + fileExtension.toLowerCase())) {
                System.err.println("File extension mismatch. Expected: " + fileExtension + ", Got: " + blobName);
                return null;
            }

            // Create a temporary file to store the downloaded content
            File tempFile = Files.createTempFile("azure-blob-", "-" + blobName).toFile();
            tempFile.deleteOnExit();

            // Download the blob to the temporary file
            try (FileOutputStream fos = new FileOutputStream(tempFile);
                 InputStream inputStream = blobClient.openInputStream()) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }

            System.out.println("Successfully downloaded file from Azure Blob Storage: " + blobName);
            return tempFile;

        } catch (IOException e) {
            System.err.println("Error downloading file from Azure Blob Storage: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("Azure Blob Storage error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Legacy method signature for backward compatibility.
     * Extracts blob name from dialogMessage parameter.
     *
     * @param dialogMessage Message containing the blob name or file identifier
     * @param parent Ignored (legacy parameter from JFileChooser)
     * @param fileExtensionDescription Ignored (legacy parameter)
     * @param fileExtension Expected file extension(s)
     * @return File object containing the downloaded content, or null if download fails
     */
    public static File ReadFile(String dialogMessage, Object parent, String fileExtensionDescription,
                                String... fileExtension) {
        // Extract blob name from dialog message or use environment variable
        String blobName = System.getenv("AZURE_BLOB_NAME");
        if (blobName == null || blobName.isEmpty()) {
            System.err.println("AZURE_BLOB_NAME environment variable not set. Cannot determine which file to download.");
            System.err.println("Please set AZURE_BLOB_NAME environment variable with the blob name to download.");
            return null;
        }

        String ext = (fileExtension != null && fileExtension.length > 0) ? fileExtension[0] : null;
        return ReadFile(blobName, ext);
    }

    /**
     * Creates a BlobServiceClient using either connection string or managed identity.
     * In production, managed identity is preferred for security.
     */
    private static BlobServiceClient createBlobServiceClient() {
        if (useConnectionString && connectionString != null) {
            // Use connection string (for development/testing)
            return new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();
        } else {
            // Use managed identity (recommended for production)
            String accountName = System.getenv("AZURE_STORAGE_ACCOUNT_NAME");
            if (accountName == null || accountName.isEmpty()) {
                throw new IllegalStateException(
                        "Azure Storage configuration missing. Set either:\n" +
                        "1. azure.storage.connection-string property, OR\n" +
                        "2. AZURE_STORAGE_ACCOUNT_NAME environment variable for managed identity"
                );
            }
            String endpoint = String.format("https://%s.blob.core.windows.net", accountName);
            return new BlobServiceClientBuilder()
                    .endpoint(endpoint)
                    .credential(new DefaultAzureCredentialBuilder().build())
                    .buildClient();
        }
    }

    /**
     * Lists all blobs in the configured container with a specific extension.
     * Useful for discovering available files in Azure Blob Storage.
     *
     * @param fileExtension File extension to filter by (e.g., "csv")
     * @return Array of blob names matching the extension
     */
    public static String[] listBlobs(String fileExtension) {
        try {
            BlobServiceClient blobServiceClient = createBlobServiceClient();
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            
            return containerClient.listBlobs().stream()
                    .map(blobItem -> blobItem.getName())
                    .filter(name -> fileExtension == null || name.toLowerCase().endsWith("." + fileExtension.toLowerCase()))
                    .toArray(String[]::new);
        } catch (Exception e) {
            System.err.println("Error listing blobs from Azure Blob Storage: " + e.getMessage());
            e.printStackTrace();
            return new String[0];
        }
    }
}
