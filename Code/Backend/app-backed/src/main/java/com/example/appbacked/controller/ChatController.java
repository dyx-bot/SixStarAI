package com.example.appbacked.controller;

import com.example.appbacked.dto.ChatRequest;
import com.example.appbacked.dto.ChatResponse;
import com.example.appbacked.service.AnythingLLMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
@RestController
@RequestMapping("/api")
public class ChatController {

    private final AnythingLLMService anythingLLMService;

    public ChatController(AnythingLLMService anythingLLMService) {
        this.anythingLLMService = anythingLLMService;
    }

    @PostMapping("/chat")
    public Mono<ChatResponse> chat(@RequestBody ChatRequest request) {
        return anythingLLMService.chat(request);
    }
}