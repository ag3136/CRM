package crm.config;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class AzureBlobStorageService {

    private final BlobContainerClient blobContainerClient;

    public AzureBlobStorageService(@Value("${azure.storage.connection-string}") String connectionString,
                                   @Value("${azure.storage.container-name}") String containerName) {
        this.blobContainerClient = new BlobContainerClientBuilder()
                .connectionString(connectionString)
                .containerName(containerName)
                .buildClient();
        if (!this.blobContainerClient.exists()) {
            this.blobContainerClient.create();
        }
    }

    public String uploadText(String blobName, String content) {
        byte[] payload = content.getBytes(StandardCharsets.UTF_8);
        BlobClient blobClient = blobContainerClient.getBlobClient(blobName);
        blobClient.upload(new ByteArrayInputStream(payload), payload.length, true);
        return blobClient.getBlobUrl();
    }

    public String uploadStream(String blobName, InputStream inputStream, long length) throws IOException {
        BlobClient blobClient = blobContainerClient.getBlobClient(blobName);
        blobClient.upload(inputStream, length, true);
        return blobClient.getBlobUrl();
    }

    public InputStream download(String blobName) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        blobContainerClient.getBlobClient(blobName).download(outputStream);
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    public String createBlobName(String prefix, String originalName) {
        return prefix + "/" + UUID.randomUUID().toString() + "-" + originalName;
    }
}
