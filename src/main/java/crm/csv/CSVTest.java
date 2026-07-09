package crm.csv;

import com.opencsv.CSVReader;
import crm.utils.ReadDataUtils;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for reading and processing CSV data from Amazon S3.
 *
 * <p>The previous implementation used {@code java.io.File} and a desktop
 * {@code JFileChooser} to locate a CSV file on the local file system.  This
 * approach is incompatible with cloud / container environments where the host
 * file system is ephemeral and no GUI is available.
 *
 * <p>The replacement reads the CSV directly from Amazon S3 via
 * {@link ReadDataUtils#readFileFromS3(S3Client, String)}, eliminating all
 * host-level file-system dependencies (12-factor app principle VI –
 * Processes).
 *
 * <p>Required environment variables:
 * <ul>
 *   <li>{@code S3_BUCKET_NAME}  – name of the S3 bucket that holds the CSV</li>
 *   <li>{@code CSV_OBJECT_KEY}  – S3 object key of the CSV file
 *       (e.g. {@code "data/input.csv"}); defaults to {@code "data/input.csv"}
 *       when not set</li>
 * </ul>
 */
public class CSVTest {

    public static void main(String[] args) {
        // Resolve the S3 object key from an environment variable so no path
        // is hard-coded in the compiled artifact.
        String objectKey = System.getenv("CSV_OBJECT_KEY");
        if (objectKey == null || objectKey.isEmpty()) {
            objectKey = "data/input.csv";
        }

        // Build a default S3Client using the standard AWS credential / region
        // provider chain (environment variables, instance profile, etc.).
        S3Client s3Client = S3Client.create();

        // Retrieve the CSV content from S3 as an InputStream – no local file
        // system access required.
        InputStream csvStream = ReadDataUtils.readFileFromS3(s3Client, objectKey);
        if (csvStream == null) {
            System.err.println("Could not retrieve CSV from S3. Check S3_BUCKET_NAME and CSV_OBJECT_KEY.");
            return;
        }

        CSVReader reader;
        List<Object[]> data = new ArrayList<>();
        try {
            reader = new CSVReader(new InputStreamReader(csvStream));
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
