package crm.utils;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.InputStream;

/**
 * Utility class for reading data from Amazon S3.
 * Replaces the previous desktop JFileChooser-based file selection with
 * cloud-native S3 object retrieval using AWS SDK for Java v2.
 */
public class ReadDataUtils {

    /**
     * Reads a file from Amazon S3 and returns its InputStream.
     *
     * @param s3Client   An initialized S3Client instance (AWS SDK v2)
     * @param bucketName The name of the S3 bucket (configured via environment variable AWS_S3_BUCKET_NAME)
     * @param objectKey  The S3 object key (path within the bucket) of the file to read
     * @return InputStream of the S3 object content, or null if retrieval fails
     */
    public static InputStream readFileFromS3(S3Client s3Client, String bucketName, String objectKey) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();
            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            System.out.println("Successfully retrieved S3 object: " + objectKey + " from bucket: " + bucketName);
            return s3Object;
        } catch (Exception e) {
            System.err.println("Failed to read file from S3 bucket [" + bucketName + "] key [" + objectKey + "]: " + e.getMessage());
            return null;
        }
    }

}
