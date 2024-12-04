package com.backend.backend.Web;

import com.backend.backend.Service.AzureBlobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("blob")
@CrossOrigin(origins = "http://localhost:8081")
public class BlobStorageController {

    @Autowired
    private AzureBlobService azureBlobService;

    /**
     * Get a list of documents in the specified subject and category folder
     */
    @GetMapping("/getDocuments")
    public ResponseEntity<List<String>> getDocuments(@RequestParam("subject") String subject, @RequestParam("category") String category) {
        List<String> documentNames = azureBlobService.getDocuments(subject, category);
        if (documentNames != null) {
            return ResponseEntity.ok(documentNames);
        } else {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Download a specific document from the Azure Blob Storage
     */
    @GetMapping("/downloadDocument/{fileName}")
    public ResponseEntity<byte[]> downloadDocument(@RequestParam("subject") String subject, @RequestParam("category") String category, @PathVariable String fileName) {
        byte[] fileContent = azureBlobService.downloadDocument(subject, category, fileName);
        if (fileContent != null) {
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
        } else {
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
        String response = azureBlobService.uploadDocument(subject, category, file);
        if (response.startsWith("File uploaded successfully")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(500).body(response);
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
        String response = azureBlobService.deleteDocument(subject, category, fileName);
        if (response.startsWith("File deleted successfully")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).body(response);
        }
    }
}
