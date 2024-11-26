package Entities;

public class FileMetadata {
    private String fileName;
    private String fileUrl;
    private String contentType;
    private long size;

    public FileMetadata(String fileName, String fileUrl, String contentType, long size) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.contentType = contentType;
        this.size = size;
    }

    // Getters and setters (if needed)
}
