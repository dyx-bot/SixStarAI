package com.example.appbacked.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ChatHistoryTest {

    private ChatHistory chatHistory;

    @BeforeEach
    void setUp() {
        chatHistory = new ChatHistory();
    }

    @Test
    void chatHistoryShouldBeCreatedWithNoArgsConstructor() {
        ChatHistory history = new ChatHistory();
        assertNotNull(history);
    }

    @Test
    void chatHistoryShouldBeCreatedWithAllArgsConstructor() {
        String sessionId = "test-session";
        String message = "Test message";
        String response = "Test response";

        ChatHistory history = new ChatHistory(sessionId, message, response);

        assertNotNull(history);
        assertEquals(sessionId, history.getSessionId());
        assertEquals(message, history.getMessage());
        assertEquals(response, history.getResponse());
        assertNotNull(history.getCreatedAt());
    }

    @Test
    void idGetterAndSetterShouldWork() {
        Long id = 1L;
        chatHistory.setId(id);
        assertEquals(id, chatHistory.getId());
    }

    @Test
    void sessionIdGetterAndSetterShouldWork() {
        String sessionId = "test-session";
        chatHistory.setSessionId(sessionId);
        assertEquals(sessionId, chatHistory.getSessionId());
    }

    @Test
    void messageGetterAndSetterShouldWork() {
        String message = "Test message";
        chatHistory.setMessage(message);
        assertEquals(message, chatHistory.getMessage());
    }

    @Test
    void responseGetterAndSetterShouldWork() {
        String response = "Test response";
        chatHistory.setResponse(response);
        assertEquals(response, chatHistory.getResponse());
    }

    @Test
    void createdAtGetterAndSetterShouldWork() {
        LocalDateTime createdAt = LocalDateTime.now();
        chatHistory.setCreatedAt(createdAt);
        assertEquals(createdAt, chatHistory.getCreatedAt());
    }

    @Test
    void idShouldBeNullByDefault() {
        assertNull(chatHistory.getId());
    }

    @Test
    void sessionIdShouldBeNullByDefault() {
        assertNull(chatHistory.getSessionId());
    }

    @Test
    void messageShouldBeNullByDefault() {
        assertNull(chatHistory.getMessage());
    }

    @Test
    void responseShouldBeNullByDefault() {
        assertNull(chatHistory.getResponse());
    }

    @Test
    void createdAtShouldBeNullByDefault() {
        assertNull(chatHistory.getCreatedAt());
    }

    @Test
    void createdAtShouldBeSetInAllArgsConstructor() {
        String sessionId = "test-session";
        String message = "Test message";
        String response = "Test response";

        ChatHistory history = new ChatHistory(sessionId, message, response);

        assertNotNull(history.getCreatedAt());
    }

    @Test
    void messageShouldAcceptLongContent() {
        String longMessage = "A".repeat(1000); // Max length is 1000
        chatHistory.setMessage(longMessage);
        assertEquals(longMessage, chatHistory.getMessage());
    }

    @Test
    void responseShouldAcceptLongContent() {
        String longResponse = "B".repeat(2000); // Max length is 2000
        chatHistory.setResponse(longResponse);
        assertEquals(longResponse, chatHistory.getResponse());
    }

    @Test
    void chatHistoryEntityShouldBeCreated() {
        assertNotNull(chatHistory);
        assertTrue(chatHistory instanceof ChatHistory);
    }
}
