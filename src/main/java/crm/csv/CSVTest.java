package crm.csv;

import com.opencsv.CSVReader;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Cloud-ready CSV test utility using Amazon S3 for file storage.
 * Replaces local file system access with S3 object storage.
 */
public class CSVTest {

    private static final String S3_BUCKET_NAME = System.getenv().getOrDefault("S3_BUCKET_NAME", "crm-data-bucket");
    private static final String AWS_REGION = System.getenv().getOrDefault("AWS_REGION", "us-east-1");

    public static void main(String[] args) {
        // Replace hardcoded file selection with S3 object key
        // The S3 key should be provided via environment variable or command line argument
        String s3Key = System.getenv().getOrDefault("CSV_FILE_S3_KEY", "data/sample.csv");
        
        if (args.length > 0) {
            s3Key = args[0];
        }
        
        System.out.println("Reading CSV file from S3: " + s3Key);
        
        S3Client s3Client = null;
        CSVReader reader = null;
        List<Object[]> data = new ArrayList<>();
        
        try {
            // Initialize S3 client with default credentials provider
            s3Client = S3Client.builder()
                    .region(Region.of(AWS_REGION))
                    .credentialsProvider(DefaultCredentialsProvider.create())
                    .build();

            // Create GetObject request
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(S3_BUCKET_NAME)
                    .key(s3Key)
                    .build();

            // Download the file from S3 as a stream
            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            
            // Read CSV directly from S3 stream without writing to local file system
            reader = new CSVReader(new BufferedReader(new InputStreamReader(s3Object)));
            String[] line;
            
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if (line.length > 1 && line[1].equals("QUICK SUB")) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + line[2]);
                }
            }
            
            System.out.println("Successfully processed " + data.size() + " rows from S3 CSV file");
            
        } catch (S3Exception e) {
            System.err.println("S3 error while reading CSV file: " + e.awsErrorDetails().errorMessage());
            System.err.println("Ensure S3_BUCKET_NAME and AWS_REGION environment variables are set correctly.");
            System.err.println("Bucket: " + S3_BUCKET_NAME + ", Region: " + AWS_REGION + ", Key: " + s3Key);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IO error while reading CSV file: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Clean up resources
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (s3Client != null) {
                s3Client.close();
            }
        }
        
        /*System.out.println(data.get(0)[1] + "\t" + data.get(0)[2]);
        System.out.println(data.get(1)[1] + "\t" + data.get(1)[2]);*/
    }

}
