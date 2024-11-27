package Repositories;


import java.io.InputStream;

public interface BlobStorageRepository {
    String uploadFile(String fileName, InputStream fileContent, long fileSize, String contentType);
    void deleteFile(String fileName);
    String getFileUrl(String fileName);
}


