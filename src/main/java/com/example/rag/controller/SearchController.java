package com.example.rag.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    /**
     * vector/hybrid/keyword 검색 요청 처리
     * [TODO] SearchService 주입, 검색/score/highlight 등 실제 구현
     * 
     * 예시 요청: POST /api/search { "query": "...", "type": "vector|hybrid|keyword", "top": 5 }
     * 예시 응답: { "results": [ { "id": "doc1", "score": 1.23, ... }, ... ] }
     */
    @PostMapping
    public SearchResponse search(@RequestBody SearchRequest request) {
        // [TODO] searchClient의 쿼리옵션에 따라 vector/hybrid/keyword 분기, 결과 반환
        return new SearchResponse(List.of()); // TODO: 임시 빈 결과
    }

    public record SearchRequest(String query, String type, Integer top) {}
    public record SearchResult(String id, Double score, Map<String, Object> fields) {}
    public record SearchResponse(List<SearchResult> results) {}
}
