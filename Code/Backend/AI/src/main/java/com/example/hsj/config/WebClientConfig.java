package com.example.hsj.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${anything-llm.base-url}")
    private String baseUrl;

    @Value("${anything-llm.api-key}")
    private String apiKey;

    @Bean
    public WebClient anythingLlmWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:3001/api/v1/workspace/xxx/chat")
                .defaultHeader("Authorization", "Bearer " + "P6MB6PW-4434BWN-HR4MAQT-GVTYANS")
                .build();
    }
}
