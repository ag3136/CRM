package crm.csv;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Cloud-native CSV processing utility using Azure Blob Storage.
 * Replaces local file system dependencies with Azure Blob Storage operations.
 */
public class CSVTest {

    private static final String CONTAINER_NAME = System.getenv("AZURE_STORAGE_CONTAINER_NAME") != null 
            ? System.getenv("AZURE_STORAGE_CONTAINER_NAME") 
            : "crm-files";

    public static void main(String[] args) {
        // Get blob name from environment variable for cloud-native configuration
        String blobName = System.getenv("AZURE_BLOB_NAME");
        if (blobName == null || blobName.isEmpty()) {
            System.err.println("AZURE_BLOB_NAME environment variable not set.");
            System.err.println("Please set AZURE_BLOB_NAME with the CSV file name to process.");
            System.err.println("Example: export AZURE_BLOB_NAME=data.csv");
            return;
        }

        // Validate CSV file extension
        if (!blobName.toLowerCase().endsWith(".csv")) {
            System.err.println("Error: File must be a CSV file. Got: " + blobName);
            return;
        }

        try {
            // Create Azure Blob Storage client
            BlobServiceClient blobServiceClient = createBlobServiceClient();
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);
            BlobClient blobClient = containerClient.getBlobClient(blobName);

            // Process CSV directly from Azure Blob Storage stream
            List<Object[]> data = new ArrayList<>();
            
            try (InputStream inputStream = blobClient.openInputStream();
                 InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                 CSVReader reader = new CSVReader(inputStreamReader)) {
                
                String[] line;
                while ((line = reader.readNext()) != null) {
                    data.add(line);
                    if (line.length > 1 && line[1].equals("QUICK SUB")) {
                        System.out.println(line[0] + "\t" + line[1] + "\t" + line[2]);
                    }
                }
                
                System.out.println("Successfully processed " + data.size() + " rows from Azure Blob Storage: " + blobName);
                
            } catch (IOException e) {
                System.err.println("Error reading CSV from Azure Blob Storage: " + e.getMessage());
                e.printStackTrace();
            }
            
        } catch (Exception e) {
            System.err.println("Azure Blob Storage error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Creates a BlobServiceClient using either connection string or managed identity.
     * In production, managed identity is preferred for security.
     */
    private static BlobServiceClient createBlobServiceClient() {
        String connectionString = System.getenv("AZURE_STORAGE_CONNECTION_STRING");
        
        if (connectionString != null && !connectionString.isEmpty()) {
            // Use connection string (for development/testing)
            return new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();
        } else {
            // Use managed identity (recommended for production)
            String accountName = System.getenv("AZURE_STORAGE_ACCOUNT_NAME");
            if (accountName == null || accountName.isEmpty()) {
                throw new IllegalStateException(
                        "Azure Storage configuration missing. Set either:\n" +
                        "1. AZURE_STORAGE_CONNECTION_STRING environment variable, OR\n" +
                        "2. AZURE_STORAGE_ACCOUNT_NAME environment variable for managed identity"
                );
            }
            String endpoint = String.format("https://%s.blob.core.windows.net", accountName);
            return new BlobServiceClientBuilder()
                    .endpoint(endpoint)
                    .credential(new DefaultAzureCredentialBuilder().build())
                    .buildClient();
        }
    }
}
