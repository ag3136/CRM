package crm.utils;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Cloud-ready utility for reading files from Google Cloud Storage.
 * Replaces local file system dependencies with GCS integration.
 */
public class ReadDataUtils {

    private static final Logger logger = LoggerFactory.getLogger(ReadDataUtils.class);
    
    // GCS configuration from environment variables
    private static final String GCS_BUCKET_NAME = System.getenv().getOrDefault("GCS_BUCKET_NAME", "crm-data-bucket");
    private static final String GCS_PROJECT_ID = System.getenv().getOrDefault("GCP_PROJECT_ID", "");

    /**
     * Downloads a file from Google Cloud Storage to a temporary local file.
     * This method replaces the GUI-based file chooser with cloud storage integration.
     * 
     * @param blobName The name/path of the file in GCS bucket
     * @param fileExtension Expected file extension for validation
     * @return File object pointing to the downloaded temporary file, or null if download fails
     */
    public static File readFileFromGCS(String blobName, String... fileExtension) {
        try {
            // Initialize GCS client
            Storage storage;
            if (GCS_PROJECT_ID != null && !GCS_PROJECT_ID.isEmpty()) {
                storage = StorageOptions.newBuilder()
                        .setProjectId(GCS_PROJECT_ID)
                        .build()
                        .getService();
            } else {
                // Use default credentials and project
                storage = StorageOptions.getDefaultInstance().getService();
            }

            // Validate file extension if provided
            if (fileExtension != null && fileExtension.length > 0) {
                boolean validExtension = false;
                for (String ext : fileExtension) {
                    if (blobName.toLowerCase().endsWith("." + ext.toLowerCase())) {
                        validExtension = true;
                        break;
                    }
                }
                if (!validExtension) {
                    logger.error("File {} does not have a valid extension. Expected: {}", blobName, String.join(", ", fileExtension));
                    return null;
                }
            }

            // Download blob from GCS
            Blob blob = storage.get(GCS_BUCKET_NAME, blobName);
            if (blob == null) {
                logger.error("File {} not found in GCS bucket {}", blobName, GCS_BUCKET_NAME);
                return null;
            }

            // Create temporary file
            String fileName = blobName.substring(blobName.lastIndexOf('/') + 1);
            File tempFile = Files.createTempFile("gcs-download-", "-" + fileName).toFile();
            tempFile.deleteOnExit();

            // Download content to temporary file
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                blob.downloadTo(fos);
            }

            logger.info("Successfully downloaded file {} from GCS bucket {} to temporary location", blobName, GCS_BUCKET_NAME);
            return tempFile;

        } catch (IOException e) {
            logger.error("Error downloading file from GCS: {}", e.getMessage(), e);
            return null;
        } catch (Exception e) {
            logger.error("Unexpected error accessing GCS: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Legacy method signature maintained for backward compatibility.
     * Now delegates to GCS-based implementation.
     * 
     * @deprecated Use readFileFromGCS(String blobName, String... fileExtension) instead
     * @param dialogMessage Not used in cloud implementation (kept for compatibility)
     * @param parent Not used in cloud implementation (kept for compatibility)
     * @param fileExtensionDescription Not used in cloud implementation (kept for compatibility)
     * @param fileExtension File extensions to validate
     * @return File object or null
     */
    @Deprecated
    public static File ReadFile(String dialogMessage, Object parent, String fileExtensionDescription,
                                String... fileExtension) {
        // In cloud environment, the file path should be provided via environment variable or configuration
        String gcsFilePath = System.getenv().getOrDefault("GCS_FILE_PATH", "");
        
        if (gcsFilePath.isEmpty()) {
            logger.warn("GCS_FILE_PATH environment variable not set. Cannot read file from cloud storage.");
            logger.warn("Please set GCS_FILE_PATH to the blob name in your GCS bucket.");
            return null;
        }
        
        logger.info("Reading file from GCS: {}", gcsFilePath);
        return readFileFromGCS(gcsFilePath, fileExtension);
    }

    /**
     * Uploads a file to Google Cloud Storage.
     * 
     * @param localFile The local file to upload
     * @param destinationBlobName The destination path/name in GCS bucket
     * @return true if upload successful, false otherwise
     */
    public static boolean uploadFileToGCS(File localFile, String destinationBlobName) {
        try {
            // Initialize GCS client
            Storage storage;
            if (GCS_PROJECT_ID != null && !GCS_PROJECT_ID.isEmpty()) {
                storage = StorageOptions.newBuilder()
                        .setProjectId(GCS_PROJECT_ID)
                        .build()
                        .getService();
            } else {
                storage = StorageOptions.getDefaultInstance().getService();
            }

            // Upload file
            storage.createFrom(
                    com.google.cloud.storage.BlobInfo.newBuilder(GCS_BUCKET_NAME, destinationBlobName).build(),
                    localFile.toPath()
            );

            logger.info("Successfully uploaded file {} to GCS bucket {} as {}", 
                    localFile.getName(), GCS_BUCKET_NAME, destinationBlobName);
            return true;

        } catch (IOException e) {
            logger.error("Error uploading file to GCS: {}", e.getMessage(), e);
            return false;
        } catch (Exception e) {
            logger.error("Unexpected error uploading to GCS: {}", e.getMessage(), e);
            return false;
        }
    }
}
