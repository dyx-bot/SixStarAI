package com.example.appbacked.service;

import com.example.appbacked.dto.ChatRequest;
import com.example.appbacked.dto.ChatResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnythingLLMServiceTest {

    @InjectMocks
    private AnythingLLMService anythingLLMService;

    @Mock
    private WebClient webClient;

    private static final String API_KEY = "test-api-key";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 使用反射设置私有字段
        try {
            java.lang.reflect.Field webClientField = AnythingLLMService.class.getDeclaredField("webClient");
            webClientField.setAccessible(true);
            webClientField.set(anythingLLMService, webClient);

            java.lang.reflect.Field apiKeyField = AnythingLLMService.class.getDeclaredField("apiKey");
            apiKeyField.setAccessible(true);
            apiKeyField.set(anythingLLMService, API_KEY);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set up test", e);
        }
    }

    @Test
    void testChatSuccess() {
        // 准备测试数据
        ChatRequest request = new ChatRequest();
        request.setMessage("Hello, how are you?");
        request.setMode("chat");

        ChatResponse expectedResponse = new ChatResponse();
        expectedResponse.setTextResponse("I'm fine, thank you for asking!");

        // 模拟 WebClient 链式调用
        WebClient.RequestBodyUriSpec requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec requestBodySpec = mock(WebClient.RequestBodySpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        // 设置模拟行为
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("")).thenReturn(requestBodySpec);
        when(requestBodySpec.header("Content-Type", "application/json"))
                .thenReturn(requestBodySpec); // 注意：这里 header 不改变类型
        when(requestBodySpec.header("Authorization", "Bearer " + API_KEY))
                .thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(request)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ChatResponse.class)).thenReturn(Mono.just(expectedResponse));

        // 执行测试
        Mono<ChatResponse> result = anythingLLMService.chat(request);

        // 验证结果
        assertNotNull(result);

        // 验证方法调用
        verify(webClient, times(1)).post();
    }


    @Test
    void testChatWithNullRequest() {
        // 执行测试并验证异常
        assertThrows(NullPointerException.class, () -> {
            anythingLLMService.chat(null);
        });
    }
}
