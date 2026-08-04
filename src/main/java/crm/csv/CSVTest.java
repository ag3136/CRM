package crm.csv;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.opencsv.CSVReader;
import crm.utils.ReadDataUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.List;

/**
 * Cloud-ready CSV test utility that reads CSV files from Google Cloud Storage.
 * Migrated from local file system (java.io.File) to GCS for cloud-native architecture.
 */
public class CSVTest {

    private static final Logger logger = LoggerFactory.getLogger(CSVTest.class);
    
    // GCS configuration from environment variables
    private static final String GCS_BUCKET_NAME = System.getenv().getOrDefault("GCS_BUCKET_NAME", "crm-data-bucket");
    private static final String GCS_PROJECT_ID = System.getenv().getOrDefault("GCP_PROJECT_ID", "");
    private static final String GCS_CSV_FILE_PATH = System.getenv().getOrDefault("GCS_CSV_FILE_PATH", "data/sample.csv");

    public static void main(String[] args) {
        // Cloud-ready approach: Read CSV directly from Google Cloud Storage
        // No longer using java.io.File for persistent storage
        
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

            // Get the blob (file) from GCS
            Blob blob = storage.get(GCS_BUCKET_NAME, GCS_CSV_FILE_PATH);
            
            if (blob == null) {
                logger.error("CSV file {} not found in GCS bucket {}", GCS_CSV_FILE_PATH, GCS_BUCKET_NAME);
                System.err.println("Error: CSV file not found in Google Cloud Storage.");
                System.err.println("Please ensure GCS_BUCKET_NAME and GCS_CSV_FILE_PATH environment variables are set correctly.");
                return;
            }

            logger.info("Reading CSV file {} from GCS bucket {}", GCS_CSV_FILE_PATH, GCS_BUCKET_NAME);

            // Read CSV directly from GCS using streaming (no local file needed)
            CSVReader reader = new CSVReader(new InputStreamReader(Channels.newInputStream(blob.reader())));
            List<Object[]> data = new ArrayList<>();
            
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if (line.length > 1 && line[1].equals("QUICK SUB")) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + line[2]);
                }
            }
            
            reader.close();
            logger.info("Successfully processed {} rows from CSV file", data.size());
            
        } catch (IOException e) {
            logger.error("Error reading CSV file from GCS: {}", e.getMessage(), e);
            e.printStackTrace();
        } catch (Exception e) {
            logger.error("Unexpected error processing CSV from GCS: {}", e.getMessage(), e);
            e.printStackTrace();
        }
        
        /* Alternative approach using ReadDataUtils helper (downloads to temp file):
        File document = ReadDataUtils.readFileFromGCS(GCS_CSV_FILE_PATH, "csv");
        if (document == null) {
            System.err.println("Error: Could not download CSV file from GCS");
            return;
        }
        
        CSVReader reader;
        List<Object[]> data = new ArrayList<>();
        try {
            reader = new CSVReader(new FileReader(document));
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if(line[1].equals("QUICK SUB")){
                    System.out.println(line[0] + "\t" + line[1] + "\t" + line[2]);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }

}
