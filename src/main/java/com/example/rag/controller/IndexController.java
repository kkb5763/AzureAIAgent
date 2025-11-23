package com.example.rag.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/index")
public class IndexController {

    /**
     * Azure OpenAI로 임베딩 생성 후 Azure Search에 벡터 필드+문서 함께 업로드
     * [TODO] IndexService 주입 및 Azure SDK 활용 업로드 구현
     * 
     * 예시 요청: POST /api/index { "docs": [ { "id": "doc1", "content": "..." }, ... ] }
     * 예시 응답: 204(No Content)
     */
    @PostMapping
    public ResponseEntity<Void> indexDocs(@RequestBody IndexRequest request) {
        // [TODO] docs.content 마다 embedding 생성 → searchClient.uploadDocuments 호출
        return ResponseEntity.noContent().build();
    }

    public record IndexRequest(List<Map<String, Object>> docs) { }
}
