package com.example.rag.config;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.search.documents.SearchClient;
import com.azure.search.documents.SearchClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AzureSearchConfig {

    private final AzureProperties azureProperties;

    @Bean
    public SearchClient searchClient() {
        AzureProperties.Search search = azureProperties.getSearch();
        return new SearchClientBuilder()
                .endpoint(search.getServiceEndpoint())
                .credential(new AzureKeyCredential(search.getAdminKey()))
                .indexName(search.getIndex())
                .buildClient();
    }
}
