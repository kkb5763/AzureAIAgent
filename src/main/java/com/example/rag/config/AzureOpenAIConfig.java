package com.example.rag.config;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AzureOpenAIConfig {

    private final AzureProperties azureProperties;

    @Bean
    public OpenAIClient openAIClient() {
        AzureProperties.OpenAI openai = azureProperties.getOpenai();
        return new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(openai.getKey()))
                .endpoint(openai.getEndpoint())
                .buildClient();
    }
}
