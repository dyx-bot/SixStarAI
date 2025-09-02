package com.example.appbacked.controller;

import com.example.appbacked.service.AnythingLLMService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ChatControllerTest {

    @Mock
    private AnythingLLMService anythingLLMService;

    private ChatController chatController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        chatController = new ChatController(anythingLLMService);
    }

    @Test
    void chatControllerShouldBeCreatedWithService() {
        assertNotNull(chatController);
        assertNotNull(anythingLLMService);
    }

    @Test
    void constructorShouldInitializeService() {
        AnythingLLMService service = org.mockito.Mockito.mock(AnythingLLMService.class);
        ChatController controller = new ChatController(service);

        assertNotNull(controller);
    }
}
