package com.example.rag.controller;

import com.example.rag.service.RagService;
import com.example.rag.config.AzureProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final RagService ragService;
    private final AzureProperties azureProperties;

    /**
     * Retrieval-Augmented Generation (RAG) 기반 챗봇 API
     * - 입력: question → SearchClient로 context 문서 top N 검색, OpenAI로 답 생성
     * - [TODO] 서비스/예외처리 보완, 소스 형식 등 추가 필요
     *
     * 예시 요청: POST /api/chat {"question": "Azure AI Search란?"}
     * 예시 응답: {"answer": "...", "sources": ["doc1", ...]}
     */
    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        var answerResult = ragService.askQuestion(
                request.question(),
                azureProperties.getSearch().getIndex(),
                azureProperties.getOpenai().getChatgptDeployment(),
                azureProperties.getOpenai().getApiVersion()
        );
        return ResponseEntity.ok(new ChatResponse(
                answerResult.answer(),
                answerResult.sources()
        ));
    }

    // DTOs: 요청/응답 전용 클래스
    public record ChatRequest(String question) { }
    public record ChatResponse(String answer, List<String> sources) { }
}
