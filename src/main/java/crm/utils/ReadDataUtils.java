package crm.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.InputStream;

/**
 * Utility class for reading data from Amazon S3 instead of local file system.
 * Replaces hard-coded file path and JFileChooser (desktop UI) dependencies
 * with cloud-native S3 object storage access using AWS SDK for Java v2.
 */
public class ReadDataUtils {

    private static final Logger logger = LoggerFactory.getLogger(ReadDataUtils.class);

    /**
     * Reads a file from Amazon S3 and returns its InputStream.
     *
     * @param s3Client   the AWS S3 client (AWS SDK v2)
     * @param bucketName the S3 bucket name (from environment variable AWS_S3_BUCKET_NAME)
     * @param s3Key      the S3 object key (path within the bucket)
     * @return InputStream of the S3 object content, or null if not found / error
     */
    public static InputStream readFileFromS3(S3Client s3Client, String bucketName, String s3Key) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();
            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            logger.info("Successfully retrieved S3 object: s3://{}/{}", bucketName, s3Key);
            return s3Object;
        } catch (S3Exception e) {
            logger.error("Failed to read file from S3 bucket '{}' with key '{}': {}", bucketName, s3Key, e.getMessage());
            return null;
        }
    }

    /**
     * Resolves the S3 bucket name from the environment variable AWS_S3_BUCKET_NAME.
     *
     * @return the bucket name configured via environment variable
     */
    public static String getS3BucketName() {
        String bucketName = System.getenv("AWS_S3_BUCKET_NAME");
        if (bucketName == null || bucketName.isEmpty()) {
            throw new IllegalStateException("Environment variable AWS_S3_BUCKET_NAME is not set.");
        }
        return bucketName;
    }

}
