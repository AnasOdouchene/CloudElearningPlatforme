package Service;



import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class BlobStorageService {

    // Hard-coded connection string and container name
    private static final String CONNECTION_STRING = "DefaultEndpointsProtocol=https;AccountName=cloudstoragee;AccountKey=teASZ9bwHpxkl+OH8LmHGxnDtWiuFYwF+MuzlAnhr8sWx0ZVgCfYQq0dSwarUC2bVTVkXRyaWArB+AStO+d6ew==;EndpointSuffix=core.windows.net";
    private static final String CONTAINER_NAME = "course-container";

    private final BlobServiceClient blobServiceClient;

    public BlobStorageService() {
        // Initialize BlobServiceClient with the connection string
        this.blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(CONNECTION_STRING)
                .buildClient();
    }

    public String uploadFile(String fileName, InputStream fileContent, long fileSize, String contentType) {
        BlobClient blobClient = blobServiceClient
                .getBlobContainerClient(CONTAINER_NAME)
                .getBlobClient(fileName);

        blobClient.upload(fileContent, fileSize, true);
        blobClient.setHttpHeaders(new BlobHttpHeaders().setContentType(contentType));

        return blobClient.getBlobUrl();
    }

    public void deleteFile(String fileName) {
        BlobClient blobClient = blobServiceClient
                .getBlobContainerClient(CONTAINER_NAME)
                .getBlobClient(fileName);

        blobClient.delete();
    }

    public String getFileUrl(String fileName) {
        BlobClient blobClient = blobServiceClient
                .getBlobContainerClient(CONTAINER_NAME)
                .getBlobClient(fileName);

        // Ensure the file exists
        if (!blobClient.exists()) {
            return null;
        }

        return blobClient.getBlobUrl();
    }
}



