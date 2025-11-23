package com.example.rag.service;

import com.azure.search.documents.SearchClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
/**
 * IndexService
 * - 문서[text, id 등]를 Azure OpenAI로 임베딩 생성 후 Azure Search에 vector 필드와 함께 업로드
 * - (참고) https://github.com/Azure/azure-search-vector-samples/tree/main/demo-java
 * - 필드명은 환경/최종 인덱스 스키마와 맞게 동적 처리 필요
 */
@Service
@RequiredArgsConstructor
public class IndexService {
    private final SearchClient searchClient;
    private final EmbeddingService embeddingService;
    private final String textFieldName = "content"; // [TODO] 실제 환경에 맞게 변경
    private final String vectorFieldName = "contentVector"; // [TODO]

    /**
     * 문서(리스트) 업로드 + embedding
     * @param docs id, content(등) 포함 map 리스트
     */
    public void uploadDocsWithEmbedding(List<Map<String, Object>> docs) {
        try {
            List<String> texts = docs.stream().map(doc -> doc.get(textFieldName).toString()).toList();
            List<List<Float>> vectors = embeddingService.createEmbeddings(texts);
            for (int i = 0; i < docs.size(); i++) {
                docs.get(i).put(vectorFieldName, vectors.get(i));
            }
            searchClient.uploadDocuments(docs);
        } catch (Exception e) {
            throw new IndexingException("문서 업로드 실패", e);
        }
    }
    public static class IndexingException extends RuntimeException {
        public IndexingException(String msg, Throwable t) {super(msg, t);}
    }
}
