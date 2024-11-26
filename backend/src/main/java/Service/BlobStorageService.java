package Service;


import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import Repositories.BlobStorageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class BlobStorageService implements BlobStorageRepository {

    private final BlobServiceClient blobServiceClient;

    @Value("${azure.storage.container-name}")
    private String containerName;

    public BlobStorageService(BlobServiceClient blobServiceClient) {
        this.blobServiceClient = blobServiceClient;
    }

    @Override
    public String uploadFile(String fileName, InputStream fileContent, long fileSize, String contentType) {
        BlobClient blobClient = blobServiceClient.getBlobContainerClient(containerName).getBlobClient(fileName);
        blobClient.upload(fileContent, fileSize, true);
        blobClient.setHttpHeaders(new BlobHttpHeaders().setContentType(contentType));
        return blobClient.getBlobUrl();
    }

    @Override
    public void deleteFile(String fileName) {
        BlobClient blobClient = blobServiceClient.getBlobContainerClient(containerName).getBlobClient(fileName);
        blobClient.delete();
    }

    @Override
    public String getFileUrl(String fileName) {
        BlobClient blobClient = blobServiceClient.getBlobContainerClient(containerName).getBlobClient(fileName);
        if (!blobClient.exists()) {
            return null;
        }
        return blobClient.getBlobUrl();
    }


}

