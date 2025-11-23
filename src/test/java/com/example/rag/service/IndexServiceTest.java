package com.example.rag.service;

import com.azure.search.documents.SearchClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.*;
/**
 * IndexService 테스트
 * - Azure SDK mock 기반 동작 검증
 */
public class IndexServiceTest {
    @Test
    void 업로드_정상() {
        SearchClient mockSearch = Mockito.mock(SearchClient.class);
        EmbeddingService mockEmbed = Mockito.mock(EmbeddingService.class);
        IndexService indexService = new IndexService(mockSearch, mockEmbed);
        List<Map<String, Object>> docs = List.of(Map.of("id", "d1", "content", "hi"));
        Mockito.when(mockEmbed.createEmbeddings(Mockito.any())).thenReturn(List.of(List.of(1.0f, 2.0f)));
        indexService.uploadDocsWithEmbedding(docs);
        Mockito.verify(mockSearch).uploadDocuments(Mockito.any());
    }
    @Test
    void 임베딩_실패시_예외() {
        SearchClient mockSearch = Mockito.mock(SearchClient.class);
        EmbeddingService mockEmbed = Mockito.mock(EmbeddingService.class);
        Mockito.when(mockEmbed.createEmbeddings(Mockito.any())).thenThrow(new RuntimeException("fail"));
        IndexService svc = new IndexService(mockSearch, mockEmbed);
        List<Map<String, Object>> docs = List.of(Map.of("id", "x", "content", "z"));
        org.junit.jupiter.api.Assertions.assertThrows(IndexService.IndexingException.class,
                () -> svc.uploadDocsWithEmbedding(docs));
    }
}
