package com.backend.backend.Web;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.models.BlobItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("blob")
@CrossOrigin(origins = "*") // Allow requests from any origin
public class BlobStorageController {

    // Inject Azure Blob Storage credentials
    @Value("${spring.cloud.azure.storage.blob.connection-string}")
    private String connectionString;

    // Name of the Azure Blob Storage container
    @Value("${spring.cloud.azure.storage.blob.container-name}")
    private String containerName;

    /**
     * Get a list of documents in the specified subject and category folder
     */
    @GetMapping("/getDocuments")
    public ResponseEntity<List<String>> getDocuments(@RequestParam("subject") String subject, @RequestParam("category") String category) {
        try {
            // Initialize the Azure Blob Service client
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

            // Define the prefix (subject/category)
            String prefix = subject + "/" + category + "/";

            // Fetch blobs with the specified prefix
            List<String> documentNames = new ArrayList<>();
            containerClient.listBlobs().forEach(blobItem -> {
                if (blobItem.getName().startsWith(prefix)) {
                    documentNames.add(blobItem.getName().replace(prefix, "")); // Remove the prefix for clean display
                }
            });

            return ResponseEntity.ok(documentNames);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }


    /**
     * Download a specific document from the Azure Blob Storage
     */
    @GetMapping("/downloadDocument/{fileName}")
    public ResponseEntity<byte[]> downloadDocument(@RequestParam("subject") String subject, @RequestParam("category") String category, @PathVariable String fileName) {
        try {
            // Initialize the Azure Blob Service client
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

            // Construct the full path of the blob
            String blobPath = subject + "/" + category + "/" + fileName; // Full path in Azure
            BlobClient blobClient = containerClient.getBlobClient(blobPath);

            // Download the file content
            byte[] fileContent = blobClient.downloadContent().toBytes();

            // Determine the content type dynamically
            String contentType = "application/octet-stream"; // Default for binary files
            if (fileName.endsWith(".pdf")) {
                contentType = "application/pdf";
            } else if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                contentType = "image/jpeg";
            } else if (fileName.endsWith(".txt")) {
                contentType = "text/plain";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(fileContent);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}
