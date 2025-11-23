package com.example.rag.service;

import com.azure.search.documents.SearchClient;
import com.azure.search.documents.models.SearchResult;
import com.azure.search.documents.models.SearchResults;
import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatChoice;
import com.azure.ai.openai.models.ChatMessage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;
import java.util.Map;

public class RagServiceTest {
    @Test
    void rag_정상동작() {
        SearchClient mockSearch = Mockito.mock(SearchClient.class);
        OpenAIClient mockOpenai = Mockito.mock(OpenAIClient.class);
        RagService svc = new RagService(mockSearch, mockOpenai);
        SearchResults fakeResults = Mockito.mock(SearchResults.class);
        SearchResult fakeResult = Mockito.mock(SearchResult.class);
        Mockito.when(fakeResult.getDocument()).thenReturn(Map.of("id", "d1", "text", "hello"));
        Mockito.when(fakeResults.getResults()).thenReturn(List.of(fakeResult));
        Mockito.when(mockSearch.search(Mockito.any(), Mockito.any())).thenReturn(fakeResults);
        ChatMessage msg = new ChatMessage().setContent("answer!");
        ChatChoice choice = new ChatChoice().setMessage(msg);
        ChatCompletions completions = new ChatCompletions().setChoices(List.of(choice));
        Mockito.when(mockOpenai.getChatCompletions(Mockito.any(), Mockito.any())).thenReturn(completions);
        RagService.RagResult result = svc.askQuestion("Q", "si", "cd", "2024-02-15-preview");
        org.junit.jupiter.api.Assertions.assertEquals("answer!", result.answer());
        org.junit.jupiter.api.Assertions.assertEquals("d1", result.sources().get(0));
    }

    @Test
    void rag_실패시_예외() {
        SearchClient mockSearch = Mockito.mock(SearchClient.class);
        OpenAIClient mockOpenai = Mockito.mock(OpenAIClient.class);
        RagService svc = new RagService(mockSearch, mockOpenai);
        Mockito.when(mockSearch.search(Mockito.any(), Mockito.any())).thenThrow(new RuntimeException("fail"));
        org.junit.jupiter.api.Assertions.assertThrows(RagService.RagException.class,
                () -> svc.askQuestion("err", "idx", "dep", "api"));
    }
}
