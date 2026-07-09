package crm.utils;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.InputStream;

/**
 * Utility class for reading data from Amazon S3.
 * Replaces the previous desktop JFileChooser / hard-coded file path approach
 * with cloud-native S3 object retrieval so the application can run in
 * ephemeral cloud / container environments without any host file-system
 * dependency.
 */
public class ReadDataUtils {

    /**
     * Retrieves an object from Amazon S3 and returns its content as an
     * {@link InputStream}.
     *
     * <p>The S3 bucket name is resolved from the environment variable
     * {@code S3_BUCKET_NAME} and the object key is supplied by the caller,
     * keeping all environment-specific values outside the compiled artifact
     * (12-factor app principle III – Config).
     *
     * @param s3Client  a pre-configured {@link S3Client} instance
     * @param objectKey the S3 object key (e.g. {@code "data/customers.csv"})
     * @return an {@link InputStream} over the object's content, or
     *         {@code null} if the bucket name environment variable is not set
     */
    public static InputStream readFileFromS3(S3Client s3Client, String objectKey) {
        String bucketName = System.getenv("S3_BUCKET_NAME");
        if (bucketName == null || bucketName.isEmpty()) {
            System.err.println("Environment variable S3_BUCKET_NAME is not set.");
            return null;
        }

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
        System.out.println("Successfully retrieved S3 object: " + objectKey
                + " from bucket: " + bucketName);
        return s3Object;
    }
}
