// AnythingLLMService.java
package com.example.appbacked.service;

import com.example.appbacked.dto.ChatRequest;
import com.example.appbacked.dto.ChatResponse;
import com.example.appbacked.entity.ChatHistory;
import com.example.appbacked.repository.ChatHistoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AnythingLLMService {

    private final WebClient webClient;
    private final String apiKey;
    private final ChatHistoryRepository chatHistoryRepository;

    public AnythingLLMService(WebClient anythingLlmWebClient,
                             @Value("${anything-llm.api-key}") String apiKey,
                             ChatHistoryRepository chatHistoryRepository) {
        this.webClient = anythingLlmWebClient;
        this.apiKey = apiKey;
        this.chatHistoryRepository = chatHistoryRepository;
    }

    public Mono<ChatResponse> chat(ChatRequest request) {
        return webClient.post()
                .uri("")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatResponse.class)
                .doOnNext(response -> {
                    // 保存聊天记录
                    ChatHistory chatHistory = new ChatHistory();
                    chatHistory.setSessionId(request.getSessionId());
                    chatHistory.setMessage(request.getMessage());
                    chatHistory.setResponse(response.getTextResponse());
                    chatHistoryRepository.save(chatHistory);
                });
    }

    public java.util.List<ChatHistory> getChatHistory(String sessionId) {
        return chatHistoryRepository.findBySessionIdOrderByCreatedAtAsc(sessionId);
    }
}
