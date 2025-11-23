package com.example.rag.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/embed")
public class EmbeddingController {

    /**
     * Azure OpenAI Embedding API를 호출해 여러 텍스트 임베딩 벡터를 생성합니다.
     * [TODO] EmbeddingService 주입 및 실제 임베딩 구현
     * 
     * 예시 요청: POST /api/embed { "texts": ["텍스트1", "텍스트2"] }
     * 예시 응답: { "embeddings": [[0.1, ...], [0.2, ...]] }
     */
    @PostMapping
    public EmbeddingResponse embed(@RequestBody EmbeddingRequest request) {
        // [TODO] OpenAIClient 통해 request.texts 에 대해 embedding 생성
        return new EmbeddingResponse(List.of()); // TODO: 임시 빈 리스트
    }

    public record EmbeddingRequest(List<String> texts) { }
    public record EmbeddingResponse(List<List<Float>> embeddings) { }
}
