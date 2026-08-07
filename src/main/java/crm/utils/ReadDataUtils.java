package crm.utils;

import crm.config.AzureBlobStorageService;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class ReadDataUtils {

    private final AzureBlobStorageService azureBlobStorageService;

    public ReadDataUtils(AzureBlobStorageService azureBlobStorageService) {
        this.azureBlobStorageService = azureBlobStorageService;
    }

    public InputStream readFile(String blobName) {
        return azureBlobStorageService.download(blobName);
    }
}
