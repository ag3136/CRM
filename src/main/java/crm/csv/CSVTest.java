package crm.csv;

import com.opencsv.CSVReader;
import crm.utils.ReadDataUtils;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV processing utility.
 * Replaced java.io.File / local FileReader usage (line 21) with Amazon S3 object retrieval
 * using AWS SDK for Java v2 via ReadDataUtils, eliminating host file system dependencies.
 */
public class CSVTest {

    public static void main(String[] args) {
        // Resolve S3 configuration from environment variables
        String bucketName = System.getenv("AWS_S3_BUCKET_NAME");
        String objectKey  = System.getenv("AWS_S3_CSV_OBJECT_KEY");

        if (bucketName == null || bucketName.isEmpty()) {
            throw new IllegalStateException(
                    "Environment variable AWS_S3_BUCKET_NAME is not set. " +
                    "Please configure it before running CSVTest.");
        }
        if (objectKey == null || objectKey.isEmpty()) {
            throw new IllegalStateException(
                    "Environment variable AWS_S3_CSV_OBJECT_KEY is not set. " +
                    "Please specify the S3 object key for the CSV file (e.g. data/input.csv).");
        }

        // Build a default S3Client (credentials resolved via the AWS default credential chain)
        S3Client s3Client = S3Client.builder().build();

        // Read the CSV file from S3 — replaces: new FileReader(document) where document was a java.io.File
        InputStream csvStream = ReadDataUtils.readFileFromS3(s3Client, bucketName, objectKey);
        if (csvStream == null) {
            System.err.println("Could not retrieve CSV from S3: s3://" + bucketName + "/" + objectKey);
            return;
        }

        CSVReader reader;
        List<Object[]> data = new ArrayList<>();
        try {
            reader = new CSVReader(new InputStreamReader(csvStream, StandardCharsets.UTF_8));
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if (line.length > 1 && line[1].equals("QUICK SUB")) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + line[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                csvStream.close();
            } catch (IOException ignored) {
            }
        }
    }
}
