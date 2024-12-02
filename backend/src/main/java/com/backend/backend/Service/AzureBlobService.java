package com.backend.backend.Service;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.models.BlobItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AzureBlobService {

    // Inject Azure Blob Storage credentials
    @Value("${spring.cloud.azure.storage.blob.connection-string}")
    private String connectionString;

    // Name of the Azure Blob Storage container
    @Value("${spring.cloud.azure.storage.blob.container-name}")
    private String containerName;

    /**
     * Get a list of documents in the specified subject and category folder
     */
    public List<String> getDocuments(String subject, String category) {
        List<String> documentNames = new ArrayList<>();
        try {
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            String prefix = subject + "/" + category + "/";

            containerClient.listBlobs().forEach(blobItem -> {
                if (blobItem.getName().startsWith(prefix)) {
                    documentNames.add(blobItem.getName().replace(prefix, ""));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return documentNames;
    }

    /**
     * Download a specific document from Azure Blob Storage
     */
    public byte[] downloadDocument(String subject, String category, String fileName) {
        try {
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            String blobPath = subject + "/" + category + "/" + fileName;
            BlobClient blobClient = containerClient.getBlobClient(blobPath);
            return blobClient.downloadContent().toBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Upload a document to Azure Blob Storage
     */
    public String uploadDocument(String subject, String category, MultipartFile file) {
        try {
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            String blobPath = subject + "/" + category + "/" + file.getOriginalFilename();
            BlobClient blobClient = containerClient.getBlobClient(blobPath);
            blobClient.upload(file.getInputStream(), file.getSize(), true);
            return "File uploaded successfully: " + file.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error uploading file: " + e.getMessage();
        }
    }

    /**
     * Delete a document from Azure Blob Storage
     */
    public String deleteDocument(String subject, String category, String fileName) {
        try {
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            String blobPath = subject + "/" + category + "/" + fileName;
            BlobClient blobClient = containerClient.getBlobClient(blobPath);

            if (blobClient.exists()) {
                blobClient.delete();
                return "File deleted successfully: " + fileName;
            } else {
                return "File not found: " + fileName;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error deleting file: " + e.getMessage();
        }
    }
}
