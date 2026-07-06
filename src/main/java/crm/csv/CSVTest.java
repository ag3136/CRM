package crm.csv;

import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV processing utility that reads CSV data from Amazon S3.
 * Replaces java.io.File-based local file operations (blocker-3, line 21)
 * with AWS SDK for Java v2 S3 client calls for cloud-native, durable storage access.
 *
 * The S3 bucket name is resolved from the environment variable AWS_S3_BUCKET_NAME.
 * The S3 object key (CSV file path within the bucket) is resolved from
 * the environment variable AWS_S3_CSV_KEY.
 */
public class CSVTest {

    private static final Logger logger = LoggerFactory.getLogger(CSVTest.class);

    public static void main(String[] args) {
        // Resolve S3 configuration from environment variables (12-factor app principle)
        String bucketName = System.getenv("AWS_S3_BUCKET_NAME");
        String s3Key = System.getenv("AWS_S3_CSV_KEY");

        if (bucketName == null || bucketName.isEmpty()) {
            throw new IllegalStateException("Environment variable AWS_S3_BUCKET_NAME is not set.");
        }
        if (s3Key == null || s3Key.isEmpty()) {
            throw new IllegalStateException("Environment variable AWS_S3_CSV_KEY is not set.");
        }

        // Build the AWS S3 client using the default credential provider chain
        // (IAM role, environment variables, ~/.aws/credentials, etc.)
        S3Client s3Client = S3Client.builder().build();

        List<Object[]> data = new ArrayList<>();

        try {
            // Replace java.io.File / FileReader with S3 GetObject call
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            logger.info("Successfully retrieved CSV from S3: s3://{}/{}", bucketName, s3Key);

            CSVReader reader = new CSVReader(new InputStreamReader(s3Object, StandardCharsets.UTF_8));
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if (line.length > 1 && line[1].equals("QUICK SUB")) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + line[2]);
                }
            }
            reader.close();
        } catch (S3Exception e) {
            logger.error("Failed to read CSV from S3 bucket '{}' with key '{}': {}", bucketName, s3Key, e.getMessage());
        } catch (IOException e) {
            logger.error("I/O error while processing CSV from S3: {}", e.getMessage());
        } finally {
            s3Client.close();
        }
    }

}
