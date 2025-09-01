package com.example.appbacked.service;


import com.example.appbacked.dto.ChatRequest;
import com.example.appbacked.dto.ChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AnythingLLMService {

    private final WebClient webClient;
    private final String apiKey;

    public AnythingLLMService(WebClient anythingLlmWebClient, @Value("${anything-llm.api-key}") String apiKey){
        this.webClient = anythingLlmWebClient;
        this.apiKey = apiKey;  // 从配置中读取 API Key
    }
    public Mono<ChatResponse> chat(ChatRequest request) {



        return webClient.post()
                .uri("")  // 如果 baseUrl 已经是完整路径，这里留空
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)  // 从配置中读取 API Key
                .bodyValue(request)  // 直接发送修改后的 ChatRequest
                .retrieve()
                .bodyToMono(ChatResponse.class);
        // .onErrorResume(e -> Mono.just(new ChatResponse("Error: " + e.getMessage())));
    }



}