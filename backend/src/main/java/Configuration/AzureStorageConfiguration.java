package Configuration;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureStorageConfiguration {

    @Bean
    public BlobServiceClient blobServiceClient() {
        return new BlobServiceClientBuilder()
                .connectionString("DefaultEndpointsProtocol=https;AccountName=cloudstoragee;AccountKey=teASZ9bwHpxkl+OH8LmHGxnDtWiuFYwF+MuzlAnhr8sWx0ZVgCfYQq0dSwarUC2bVTVkXRyaWArB+AStO+d6ew==;EndpointSuffix=core.windows.net")
                .buildClient();
    }
}

