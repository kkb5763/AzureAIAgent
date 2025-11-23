package com.example.rag.service;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.models.Embeddings;
import com.azure.ai.openai.models.EmbeddingItem;
import com.azure.ai.openai.models.EmbeddingsOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;

/**
 * EmbeddingService 단위테스트 예시
 * - mock AzureClient or 실제 연결환경 모두 적용 가능
 */
public class EmbeddingServiceTest {
    @Test
    void embedding_정상동작() {
        // given
        OpenAIClient mockClient = Mockito.mock(OpenAIClient.class);
        String deployment = "my-embedding-deploy";
        // 임의 임베딩값 mock
        Embeddings fakeEmbeddings = new Embeddings()
                .setData(List.of(new EmbeddingItem().setEmbedding(List.of(0.1f, 0.2f))));
        Mockito.when(mockClient.getEmbeddings(Mockito.anyString(), Mockito.any(EmbeddingsOptions.class)))
                .thenReturn(fakeEmbeddings);
        EmbeddingService service = new EmbeddingService(mockClient, deployment);
        // when
        List<List<Float>> result = service.createEmbeddings(List.of("hello"));
        // then
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(2, result.get(0).size());
    }

    @Test
    void embedding_앱실패시_예외() {
        OpenAIClient mockClient = Mockito.mock(OpenAIClient.class);
        Mockito.when(mockClient.getEmbeddings(Mockito.anyString(), Mockito.any())).thenThrow(new RuntimeException("실패"));
        EmbeddingService service = new EmbeddingService(mockClient, "d");
        Assertions.assertThrows(EmbeddingService.EmbeddingException.class,
                () -> service.createEmbeddings(List.of("hi")));
    }
    // [참고] 실제 Azure 환경에서는 mockClient 대신 실제 bean, 연결값 사용
}
