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

    // Getters and setters
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
