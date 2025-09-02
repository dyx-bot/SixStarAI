package com.example.appbacked.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChatRequestTest {

    private ChatRequest chatRequest;

    @BeforeEach
    void setUp() {
        chatRequest = new ChatRequest();
    }

    @Test
    void chatRequestShouldBeCreatedWithNoArgsConstructor() {
        ChatRequest request = new ChatRequest();
        assertNotNull(request);
    }

    @Test
    void chatRequestShouldBeCreatedWithMessageAndMode() {
        ChatRequest request = new ChatRequest("Hello", "chat");
        assertNotNull(request);
        assertEquals("Hello", request.getMessage());
        assertEquals("chat", request.getMode());
    }

    @Test
    void messageGetterAndSetterShouldWork() {
        chatRequest.setMessage("Test message");
        assertEquals("Test message", chatRequest.getMessage());
    }

    @Test
    void modeGetterAndSetterShouldWork() {
        chatRequest.setMode("test-mode");
        assertEquals("test-mode", chatRequest.getMode());
    }

    @Test
    void sessionIdGetterAndSetterShouldWork() {
        chatRequest.setSessionId("test-session");
        assertEquals("test-session", chatRequest.getSessionId());
    }

    @Test
    void attachmentsGetterAndSetterShouldWork() {
        List<ChatRequest.Attachment> attachments = new ArrayList<>();
        ChatRequest.Attachment attachment = new ChatRequest.Attachment();
        attachment.setName("test.txt");
        attachment.setMime("text/plain");
        attachment.setContentString("test content");
        attachments.add(attachment);

        chatRequest.setAttachments(attachments);
        assertEquals(attachments, chatRequest.getAttachments());
        assertEquals(1, chatRequest.getAttachments().size());
        assertEquals("test.txt", chatRequest.getAttachments().get(0).getName());
    }

    @Test
    void resetGetterAndSetterShouldWork() {
        chatRequest.setReset(true);
        assertTrue(chatRequest.getReset());

        chatRequest.setReset(false);
        assertFalse(chatRequest.getReset());
    }

    @Test
    void resetShouldBeNullByDefault() {
        assertNull(chatRequest.getReset());
    }

    @Test
    void sessionIdShouldBeNullByDefault() {
        assertNull(chatRequest.getSessionId());
    }

    @Test
    void attachmentsShouldBeNullByDefault() {
        assertNull(chatRequest.getAttachments());
    }

    @Test
    void messageShouldBeNullByDefault() {
        assertNull(chatRequest.getMessage());
    }

    @Test
    void modeShouldBeNullByDefault() {
        assertNull(chatRequest.getMode());
    }

    @Test
    void attachmentNoArgsConstructorShouldWork() {
        ChatRequest.Attachment attachment = new ChatRequest.Attachment();
        assertNotNull(attachment);
    }

    @Test
    void attachmentAllArgsConstructorShouldWork() {
        ChatRequest.Attachment attachment = new ChatRequest.Attachment("test.txt", "text/plain", "content");
        assertNotNull(attachment);
        assertEquals("test.txt", attachment.getName());
        assertEquals("text/plain", attachment.getMime());
        assertEquals("content", attachment.getContentString());
    }

    @Test
    void attachmentNameGetterAndSetterShouldWork() {
        ChatRequest.Attachment attachment = new ChatRequest.Attachment();
        attachment.setName("document.pdf");
        assertEquals("document.pdf", attachment.getName());
    }

    @Test
    void attachmentMimeGetterAndSetterShouldWork() {
        ChatRequest.Attachment attachment = new ChatRequest.Attachment();
        attachment.setMime("application/pdf");
        assertEquals("application/pdf", attachment.getMime());
    }

    @Test
    void attachmentContentStringGetterAndSetterShouldWork() {
        ChatRequest.Attachment attachment = new ChatRequest.Attachment();
        attachment.setContentString("PDF content");
        assertEquals("PDF content", attachment.getContentString());
    }

    @Test
    void attachmentNameShouldBeNullByDefault() {
        ChatRequest.Attachment attachment = new ChatRequest.Attachment();
        assertNull(attachment.getName());
    }

    @Test
    void attachmentMimeShouldBeNullByDefault() {
        ChatRequest.Attachment attachment = new ChatRequest.Attachment();
        assertNull(attachment.getMime());
    }

    @Test
    void attachmentContentStringShouldBeNullByDefault() {
        ChatRequest.Attachment attachment = new ChatRequest.Attachment();
        assertNull(attachment.getContentString());
    }
}
