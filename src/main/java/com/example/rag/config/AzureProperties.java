package com.example.rag.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "azure")
@Data
public class AzureProperties {
    private Search search;
    private OpenAI openai;

    @Data
    public static class Search {
        private String serviceEndpoint;      // AZURE_SEARCH_SERVICE_ENDPOINT
        private String adminKey;             // AZURE_SEARCH_ADMIN_KEY
        private String index;                // AZURE_SEARCH_INDEX
    }

    @Data
    public static class OpenAI {
        private String endpoint;                 // AZURE_OPENAI_ENDPOINT
        private String key;                      // AZURE_OPENAI_KEY
        private String embeddingDeployment;      // AZURE_OPENAI_EMBEDDING_DEPLOYMENT
        private String chatgptDeployment;        // AZURE_OPENAI_CHATGPT_DEPLOYMENT
        private String apiVersion;               // AZURE_OPENAI_API_VERSION
    }
}
