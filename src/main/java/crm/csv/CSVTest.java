package crm.csv;

import com.opencsv.CSVReader;
import crm.config.AzureBlobStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CSVTest implements CommandLineRunner {

    private final AzureBlobStorageService azureBlobStorageService;
    private final String csvBlobName;

    public CSVTest(AzureBlobStorageService azureBlobStorageService,
                   @Value("${crm.csv.blob-name:sample-data.csv}") String csvBlobName) {
        this.azureBlobStorageService = azureBlobStorageService;
        this.csvBlobName = csvBlobName;
    }

    @Override
    public void run(String... args) {
        List<Object[]> data = new ArrayList<>();
        try (Reader blobReader = new InputStreamReader(azureBlobStorageService.download(csvBlobName));
             CSVReader reader = new CSVReader(blobReader)) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if (line.length > 2 && "QUICK SUB".equals(line[1])) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + line[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
