package crm.utils;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for reading data from Amazon S3 instead of local file system.
 * Replaces hard-coded file path and JFileChooser-based file selection with
 * cloud-native S3 object retrieval using AWS SDK for Java v2.
 */
public class ReadDataUtils {

    /**
     * Retrieves an InputStream for the specified S3 object key from the configured bucket.
     * Replaces the local JFileChooser / hard-coded file path pattern with an S3 GetObject call.
     *
     * @param s3Client  an initialised AWS SDK v2 S3Client
     * @param bucketName the S3 bucket name (read from environment variable AWS_S3_BUCKET_NAME)
     * @param objectKey  the S3 object key (e.g. "data/input.csv")
     * @return InputStream of the S3 object content, or null if the object does not exist
     */
    public static InputStream readFileFromS3(S3Client s3Client, String bucketName, String objectKey) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();
            ResponseInputStream<GetObjectResponse> response = s3Client.getObject(getObjectRequest);
            System.out.println("Reading S3 object: s3://" + bucketName + "/" + objectKey);
            return response;
        } catch (Exception e) {
            System.err.println("Failed to read S3 object s3://" + bucketName + "/" + objectKey + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Lists S3 object keys in the given bucket that match the specified file extension.
     * Replaces the FileNameExtensionFilter pattern used with JFileChooser.
     *
     * @param s3Client      an initialised AWS SDK v2 S3Client
     * @param bucketName    the S3 bucket name
     * @param fileExtension file extension to filter by (e.g. "csv")
     * @return list of matching S3 object keys
     */
    public static List<String> listFilesByExtension(S3Client s3Client, String bucketName, String fileExtension) {
        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();
        ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);
        return listResponse.contents().stream()
                .map(S3Object::key)
                .filter(key -> key.toLowerCase().endsWith("." + fileExtension.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Convenience method: reads a CSV file from S3 using the bucket name resolved
     * from the environment variable AWS_S3_BUCKET_NAME.
     *
     * @param s3Client  an initialised AWS SDK v2 S3Client
     * @param objectKey the S3 object key for the CSV file
     * @return InputStream of the CSV content, or null on failure
     */
    public static InputStream readCsvFromS3(S3Client s3Client, String objectKey) {
        String bucketName = System.getenv("AWS_S3_BUCKET_NAME");
        if (bucketName == null || bucketName.isEmpty()) {
            throw new IllegalStateException(
                    "Environment variable AWS_S3_BUCKET_NAME is not set. " +
                    "Please configure it before reading files from S3.");
        }
        return readFileFromS3(s3Client, bucketName, objectKey);
    }
}
