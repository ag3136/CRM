package crm.utils;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Cloud-ready utility for reading files from Amazon S3.
 * Replaces local file system access with S3 object storage.
 */
public class ReadDataUtils {

    private static final String S3_BUCKET_NAME = System.getenv().getOrDefault("S3_BUCKET_NAME", "crm-data-bucket");
    private static final String AWS_REGION = System.getenv().getOrDefault("AWS_REGION", "us-east-1");

    /**
     * Downloads a file from Amazon S3 to a temporary local file.
     * 
     * @param s3Key The S3 object key (path) to download
     * @return File object pointing to the downloaded temporary file, or null if download fails
     */
    public static File ReadFile(String s3Key) {
        return ReadFile(s3Key, S3_BUCKET_NAME);
    }

    /**
     * Downloads a file from Amazon S3 to a temporary local file.
     * 
     * @param s3Key The S3 object key (path) to download
     * @param bucketName The S3 bucket name
     * @return File object pointing to the downloaded temporary file, or null if download fails
     */
    public static File ReadFile(String s3Key, String bucketName) {
        S3Client s3Client = null;
        File tempFile = null;
        
        try {
            // Initialize S3 client with default credentials provider
            s3Client = S3Client.builder()
                    .region(Region.of(AWS_REGION))
                    .credentialsProvider(DefaultCredentialsProvider.create())
                    .build();

            // Create GetObject request
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            // Download the file from S3
            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            
            // Create a temporary file
            String fileName = s3Key.substring(s3Key.lastIndexOf('/') + 1);
            String fileExtension = "";
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0) {
                fileExtension = fileName.substring(dotIndex);
                fileName = fileName.substring(0, dotIndex);
            }
            
            tempFile = File.createTempFile(fileName, fileExtension);
            tempFile.deleteOnExit(); // Clean up on JVM exit
            
            // Write S3 object content to temporary file
            try (FileOutputStream fos = new FileOutputStream(tempFile);
                 InputStream is = s3Object) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
            
            System.out.println("Successfully downloaded file from S3: " + s3Key + " to " + tempFile.getAbsolutePath());
            return tempFile;
            
        } catch (S3Exception e) {
            System.err.println("S3 error while downloading file: " + e.awsErrorDetails().errorMessage());
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            System.err.println("IO error while writing file: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            if (s3Client != null) {
                s3Client.close();
            }
        }
    }

    /**
     * Legacy method signature for backward compatibility.
     * This method is deprecated and should be replaced with S3-based file access.
     * 
     * @param dialogMessage Not used in cloud environment
     * @param parent Not used in cloud environment
     * @param fileExtensionDescription Not used in cloud environment
     * @param fileExtension Not used in cloud environment
     * @return null - this method is not supported in cloud environments
     * @deprecated Use ReadFile(String s3Key) instead
     */
    @Deprecated
    public static File ReadFile(String dialogMessage, Object parent, String fileExtensionDescription,
                                String... fileExtension) {
        System.err.println("WARNING: Legacy ReadFile method called. This method uses JFileChooser which is not supported in cloud environments.");
        System.err.println("Please use ReadFile(String s3Key) to download files from S3 instead.");
        System.err.println("Configure S3_BUCKET_NAME and AWS_REGION environment variables.");
        return null;
    }

}
