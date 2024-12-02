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
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("blob")
@CrossOrigin(origins = "http://127.0.0.1:5500/") // Allow requests from any origin
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

    /**
     * Upload a document to Azure Blob Storage
     */
    @PostMapping("/uploadDocument")
    public ResponseEntity<String> uploadDocument(
            @RequestParam("subject") String subject,
            @RequestParam("category") String category,
            @RequestParam("file") MultipartFile file) {
        try {
            // Initialize the Azure Blob Service client
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

            // Construct the blob path
            String blobPath = subject + "/" + category + "/" + file.getOriginalFilename();

            // Get a BlobClient to interact with the blob
            BlobClient blobClient = containerClient.getBlobClient(blobPath);

            // Upload the file content to Azure Blob Storage
            blobClient.upload(file.getInputStream(), file.getSize(), true);

            return ResponseEntity.ok("File uploaded successfully: " + file.getOriginalFilename());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error uploading file: " + e.getMessage());
        }
    }

    /**
     * Delete a document from Azure Blob Storage
     */
    @DeleteMapping("/deleteDocument/{fileName}")
    public ResponseEntity<String> deleteDocument(
            @RequestParam("subject") String subject,
            @RequestParam("category") String category,
            @PathVariable String fileName) {
        try {
            // Initialize the Azure Blob Service client
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

            // Construct the blob path
            String blobPath = subject + "/" + category + "/" + fileName;

            // Get a BlobClient to interact with the blob
            BlobClient blobClient = containerClient.getBlobClient(blobPath);

            // Delete the blob
            if (blobClient.exists()) {
                blobClient.delete();
                return ResponseEntity.ok("File deleted successfully: " + fileName);
            } else {
                return ResponseEntity.status(404).body("File not found: " + fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error deleting file: " + e.getMessage());
        }
    }
}
