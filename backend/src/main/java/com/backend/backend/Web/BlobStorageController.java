package com.backend.backend.Web;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.models.BlobDownloadResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("blob")
public class BlobStorageController {

    // Inject Azure Blob Storage credentials
    @Value("${spring.cloud.azure.storage.blob.connection-string}")
    private String connectionString;

    // Endpoint to upload a new file (e.g., PDF)
    @PostMapping("/uploadBlobFile")
    public String uploadBlobFile(@RequestParam("file") MultipartFile file) throws IOException {
        // Build a BlobServiceClient using the connection string
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();

        // Get a reference to the container (e.g., 'course-container')
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient("course-container");

        // Get the uploaded file's InputStream
        InputStream inputStream = file.getInputStream();

        // Create a BlobClient and upload the file
        BlobClient blobClient = containerClient.getBlobClient(file.getOriginalFilename());

        // Upload the file to the blob container
        blobClient.upload(inputStream, file.getSize(), true); // The 'true' flag overwrites any existing blob with the same name

        return "File uploaded successfully: " + file.getOriginalFilename();
    }


    // Endpoint to read a file from Azure Blob Storage
    @GetMapping("/readBlobFile/{filename}")
    public ResponseEntity<?> readBlobFile(@PathVariable String filename) throws IOException {
        // Build a BlobServiceClient using the connection string
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();

        // Get a reference to the container (e.g., 'course-container')
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient("course-container");

        // Get the BlobClient for the specific file
        BlobClient blobClient = containerClient.getBlobClient(filename);

        if (!blobClient.exists()) {
            return ResponseEntity.notFound().build();
        }

        // Download the content of the blob as BinaryData
        BinaryData binaryData = blobClient.downloadContent();

        // Convert BinaryData to byte array using toBytes()
        byte[] fileBytes = binaryData.toBytes();

        // Determine file type based on extension
        String fileExtension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        MediaType mediaType;

        switch (fileExtension) {
            case "txt":
                mediaType = MediaType.TEXT_PLAIN;
                String content = new String(fileBytes, StandardCharsets.UTF_8);
                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .body(content); // Return text content as plain text
            case "pdf":
                mediaType = MediaType.APPLICATION_PDF;
                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                        .body(fileBytes); // Return PDF content as byte array
            case "png":
            case "jpg":
            case "jpeg":
                mediaType = fileExtension.equals("png") ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG;
                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .body(fileBytes); // Return image as byte array
            default:
                return ResponseEntity.status(415).body("Unsupported file type");
        }
    }

    // Endpoint to delete a file from Azure Blob Storage
    @DeleteMapping("/deleteBlobFile/{fileName}")
    public String deleteBlobFile(@PathVariable String fileName) {
        // Build a BlobServiceClient using the connection string
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();

        // Get a reference to the container (e.g., 'course-container')
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient("course-container");

        // Get the BlobClient for the specified file
        BlobClient blobClient = containerClient.getBlobClient(fileName);

        // Delete the blob (file) from Azure Storage
        blobClient.delete();

        return "File deleted successfully: " + fileName;
    }
}
