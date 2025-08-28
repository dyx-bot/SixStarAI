package com.example.hsj.controller;
import com.example.hsj.dto.ChatRequest;
import com.example.hsj.dto.ChatResponse;
import com.example.hsj.service.AnythingLLMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final AnythingLLMService anythingLLMService;

    public ChatController(AnythingLLMService anythingLLMService) {
        this.anythingLLMService = anythingLLMService;
    }

    @PostMapping
    public Mono<ChatResponse> chat(@RequestBody ChatRequest request) {
        return anythingLLMService.chat(request);
    }
}