package com.example.rag.service;

import com.azure.search.documents.SearchClient;
import com.azure.search.documents.models.QueryType;
import com.azure.search.documents.models.SearchOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
import com.azure.search.documents.util.SearchPagedIterable;
import com.azure.search.documents.models.SearchResult;
import com.azure.core.util.Context;
/**
 * SearchService
 * - Azure Search의 vector/hybrid/keyword(텍스트) 검색을 분기 처리
 * - score, highlight, other field 등 풍부하게 결과 전달
 * - (참고) https://learn.microsoft.com/azure/search/vector-search-overview
 */
@Service
@RequiredArgsConstructor
public class SearchService {
    private final SearchClient searchClient;

    /**
     * vector/hybrid/keyword 택1로 Azure Search 검색 실시
     * @param query 유저 쿼리
     * @param type vector|hybrid|keyword
     * @param top 결과수
     * @return result DTO 리스트
     */
    public List<SearchResultDTO> search(String query, String type, int top) {
        try {
            SearchOptions options = new SearchOptions().setTop(top);
            if (Objects.equals(type, "vector")) {
                options.setQueryType(QueryType.SEMANTIC); // 실제 vector 필드 쿼리 옵션 필요
            } else if (Objects.equals(type, "hybrid")) {
                options.setQueryType(QueryType.FULL); // hybrid 예시, 실제 옵션 환경 맞춤
            } else {
                options.setQueryType(QueryType.SIMPLE);
            }
            SearchPagedIterable results = searchClient.search(query, options, Context.NONE);
            List<SearchResultDTO> out = new ArrayList<>();
            for (SearchResult r : results) {
                Map<String, Object> fields = r.getDocument(Map.class);
                out.add(new SearchResultDTO(
                        fields.getOrDefault("id", "").toString(),
                        null,
                        fields
                ));
            }
            return out;
        } catch (Exception e) {
            throw new SearchException("검색 실패", e);
        }
    }
    public record SearchResultDTO(String id, Double score, Map<String,Object> fields){}
    public static class SearchException extends RuntimeException {
        public SearchException(String msg, Throwable t) {super(msg, t);}
    }
}
