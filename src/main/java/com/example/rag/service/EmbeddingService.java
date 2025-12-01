package com.example.rag.service;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.models.EmbeddingItem;
import com.azure.ai.openai.models.EmbeddingsOptions;
import com.azure.ai.openai.models.Embeddings;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
/**
 * EmbeddingService
 * - Azure OpenAI의 embedding 모델을 호출해 텍스트 벡터를 생성하는 서비스
 * - (참고) https://learn.microsoft.com/azure/ai-services/openai/how-to/embeddings
 * - 예외 발생 시 EmbeddingException으로 래핑
 */
@Service
@RequiredArgsConstructor
public class EmbeddingService {
    private final OpenAIClient openAIClient;
    @Value("${azure.openai.embedding-deployment}")
    private String embeddingDeployment;

    /**
     * 여러 텍스트에 대해 임베딩 벡터 생성
     * @param texts 임베딩할 문자열 리스트
     * @return 임베딩 벡터 목록(List<List<Float>>)
     */
    public List<List<Float>> createEmbeddings(List<String> texts) {
        try {
            EmbeddingsOptions options = new EmbeddingsOptions(texts);
            Embeddings embeddings = openAIClient.getEmbeddings(embeddingDeployment, options);
            // TODO: 타입 불일치 해결 필요 (OpenAI SDK 버전에 따라 반환 타입 확인)
            // return embeddings.getData().stream()
            //         .map(EmbeddingItem::getEmbedding)
            //         .collect(Collectors.toList());
            return null;
        } catch (Exception e) {
            throw new EmbeddingException("임베딩 생성 실패", e);
        }
    }
    public static class EmbeddingException extends RuntimeException {
        public EmbeddingException(String msg, Throwable t) {super(msg, t);}
    }
}
