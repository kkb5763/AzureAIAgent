package com.example.rag.service;

import com.azure.search.documents.SearchClient;
import com.azure.search.documents.models.SearchOptions;
import com.azure.search.documents.models.SearchResult;
import com.azure.search.documents.models.SearchResults;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.*;

public class SearchServiceTest {
    @Test
    void search_키워드분기_정상동작() {
        SearchClient mockClient = Mockito.mock(SearchClient.class);
        SearchService svc = new SearchService(mockClient);
        SearchResults mockResults = Mockito.mock(SearchResults.class);
        SearchResult mockResult = Mockito.mock(SearchResult.class);
        Map<String,Object> doc = Map.of("id", "doc1", "foo", "bar");
        Mockito.when(mockResult.getDocument()).thenReturn(doc);
        Mockito.when(mockResult.getScore()).thenReturn(1.5);
        Mockito.when(mockResults.getResults()).thenReturn(List.of(mockResult));
        Mockito.when(mockClient.search(Mockito.any(), Mockito.any(SearchOptions.class))).thenReturn(mockResults);
        List<SearchService.SearchResultDTO> res = svc.search("hi", "keyword", 3);
        org.junit.jupiter.api.Assertions.assertEquals(1, res.size());
        org.junit.jupiter.api.Assertions.assertEquals("doc1", res.get(0).id());
        org.junit.jupiter.api.Assertions.assertEquals(1.5, res.get(0).score());
    }
    @Test
    void search_예외() {
        SearchClient mockClient = Mockito.mock(SearchClient.class);
        Mockito.when(mockClient.search(Mockito.any(), Mockito.any(SearchOptions.class))).thenThrow(new RuntimeException("fail"));
        SearchService svc = new SearchService(mockClient);
        org.junit.jupiter.api.Assertions.assertThrows(SearchService.SearchException.class,
                () -> svc.search("hi", "vec", 1));
    }
}
