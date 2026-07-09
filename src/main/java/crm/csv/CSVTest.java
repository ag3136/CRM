package crm.csv;

import com.opencsv.CSVReader;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV processing utility that reads CSV data from Amazon S3.
 * Replaces the previous java.io.File-based local file operations with
 * cloud-native S3 object retrieval using AWS SDK for Java v2.
 *
 * The S3 bucket name and object key are resolved from environment variables:
 *   AWS_S3_BUCKET_NAME  - the target S3 bucket
 *   AWS_S3_CSV_KEY      - the S3 object key for the CSV file
 */
public class CSVTest {

    public static void main(String[] args) {
        // Resolve S3 configuration from environment variables (12-factor app principle)
        String bucketName = System.getenv("AWS_S3_BUCKET_NAME");
        String csvObjectKey = System.getenv("AWS_S3_CSV_KEY");

        if (bucketName == null || bucketName.isEmpty()) {
            System.err.println("Environment variable AWS_S3_BUCKET_NAME is not set.");
            return;
        }
        if (csvObjectKey == null || csvObjectKey.isEmpty()) {
            System.err.println("Environment variable AWS_S3_CSV_KEY is not set.");
            return;
        }

        // Build an S3Client using the default credential provider chain (IAM role / env vars)
        S3Client s3Client = S3Client.builder().build();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(csvObjectKey)
                .build();

        List<Object[]> data = new ArrayList<>();
        try {
            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            System.out.println("Successfully retrieved CSV from S3: s3://" + bucketName + "/" + csvObjectKey);

            CSVReader reader = new CSVReader(new InputStreamReader(s3Object));
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if (line.length > 1 && line[1].equals("QUICK SUB")) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + line[2]);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            s3Client.close();
        }
    }

}
