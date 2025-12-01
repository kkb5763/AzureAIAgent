package com.example.rag.service;

import com.azure.search.documents.SearchClient;
import com.azure.search.documents.models.QueryType;
import com.azure.search.documents.models.SearchOptions;
import com.azure.search.documents.util.SearchPagedIterable;
import com.azure.search.documents.models.SearchResult;
import com.azure.core.util.Context;
// import com.azure.search.documents.SearchDocument; // 완전히 제거 (해당 클래스 없으므로)
import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.models.ChatCompletionsOptions;
// import com.azure.ai.openai.ChatMessage; // 실제 버전에 맞는 클래스로 수정 필요
// import com.azure.ai.openai.ChatRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * RagService
 * - Azure Search로 벡터+키워드 하이브리드 검색(TopN)
 * - context 추출 및 OpenAI ChatCompletions으로 RAG 답변 생성
 * - (참고) https://github.com/Azure/azure-search-vector-samples/tree/main/demo-python, demo-java
 * - 예외 발생시 RagException
 */
@Service
@RequiredArgsConstructor
public class RagService {
    private final SearchClient searchClient;
    private final OpenAIClient openAIClient;

    /**
     * RAG : 검색-임베딩-ChatCompletions 한 사이클 실행
     * @param question 질문
     * @param searchIndex 인덱스명(미사용시 삭제)
     * @param chatDeployment chat 모델명
     * @param apiVersion OpenAI API버전
     * @return RagResult(answer, sources)
     */
    public RagResult askQuestion(String question, String searchIndex, String chatDeployment, String apiVersion) {
        try {
            // 1. Azure Search로 vector+키워드 hybrid top 5 검색
            SearchOptions searchOptions = new SearchOptions().setQueryType(QueryType.SIMPLE).setTop(5);
            SearchPagedIterable searchResults = searchClient.search(question, searchOptions, Context.NONE);
            List<String> docContents = new ArrayList<>();
            List<String> docIds = new ArrayList<>();
            for (SearchResult result : searchResults) {
                Map<String, Object> doc = result.getDocument(Map.class); // Map.class를 명시해야 빌드 통과!
                docContents.add(doc.toString()); // [TODO] 필요 필드 가공/정제
                docIds.add(doc.getOrDefault("id", "").toString());
            }
            String context = String.join("\n\n", docContents);

            // 2. OpenAI ChatCompletions로 답변 생성
            // TODO: OpenAI 메시지 구성 아래 부분은 버전 오류로 임시 주석 처리 (최신 SDK 가이드 참고)
            // List<com.azure.ai.openai.ChatMessage> messages = List.of(
            //         new com.azure.ai.openai.ChatMessage(com.azure.ai.openai.ChatRole.SYSTEM, "You are a helpful assistant that answers based ONLY on the context."),
            //         new com.azure.ai.openai.ChatMessage(com.azure.ai.openai.ChatRole.USER, "Context:\n" + context + "\n\nQuestion:" + question)
            // );
            // ChatCompletionsOptions options = new ChatCompletionsOptions(messages);
            // String answer = openAIClient.getChatCompletions(chatDeployment, options)
            //         .getChoices().get(0).getMessage().getContent();
            String answer = "[TODO: OpenAI 답변 생성 구현 필요]";
            return new RagResult(answer, docIds);
        } catch (Exception e) {
            throw new RagException("RAG 과정 중 실패", e);
        }
    }

    // 결과 DTO
    public record RagResult(String answer, List<String> sources) { }
    public static class RagException extends RuntimeException {
        public RagException(String msg, Throwable t) {super(msg, t);}
    }
}
