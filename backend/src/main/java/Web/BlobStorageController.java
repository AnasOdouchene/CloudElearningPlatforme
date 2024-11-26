package Web;


import Entities.FileMetadata;
import Service.BlobStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
public class BlobStorageController {

    private final BlobStorageService blobStorageService;

    public BlobStorageController(BlobStorageService blobStorageService) {
        this.blobStorageService = blobStorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<FileMetadata> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String fileUrl = blobStorageService.uploadFile(
                file.getOriginalFilename(),
                file.getInputStream(),
                file.getSize(),
                file.getContentType()
        );

        FileMetadata metadata = new FileMetadata(
                file.getOriginalFilename(),
                fileUrl,
                file.getContentType(),
                file.getSize()
        );

        return ResponseEntity.ok(metadata);
    }

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        blobStorageService.deleteFile(fileName);
        return ResponseEntity.ok("File deleted successfully");
    }

    @GetMapping("/url/{fileName}")
    public ResponseEntity<String> getFileUrl(@PathVariable String fileName) {
        String fileUrl = blobStorageService.getFileUrl(fileName);
        if (fileUrl == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
        }
        return ResponseEntity.ok(fileUrl);
    }


}

