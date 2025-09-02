package com.example.appbacked.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReplyTest {

    private Reply reply;

    @BeforeEach
    void setUp() {
        reply = new Reply();
    }

    @Test
    void testGetSetId() {
        // Given
        Long id = 1L;

        // When
        reply.setId(id);

        // Then
        assertEquals(id, reply.getId());
    }

    @Test
    void testGetSetUserId() {
        // Given
        Long userId = 100L;

        // When
        reply.setUserId(userId);

        // Then
        assertEquals(userId, reply.getUserId());
    }

    @Test
    void testGetSetPostId() {
        // Given
        Long postId = 200L;

        // When
        reply.setPostId(postId);

        // Then
        assertEquals(postId, reply.getPostId());
    }

    @Test
    void testGetSetContent() {
        // Given
        String content = "Test reply content";

        // When
        reply.setContent(content);

        // Then
        assertEquals(content, reply.getContent());
    }

    @Test
    void testGetSetContent_EmptyValue() {
        // Given
        String content = "";

        // When
        reply.setContent(content);

        // Then
        assertEquals(content, reply.getContent());
    }

    @Test
    void testGetSetContent_NullValue() {
        // When
        reply.setContent(null);

        // Then
        assertNull(reply.getContent());
    }

    @Test
    void testGetSetCreatedAt() {
        // Given
        LocalDateTime createdAt = LocalDateTime.now();

        // When
        reply.setCreatedAt(createdAt);

        // Then
        assertEquals(createdAt, reply.getCreatedAt());
    }

    @Test
    void testGetSetCreatedAt_NullValue() {
        // When
        reply.setCreatedAt(null);

        // Then
        assertNull(reply.getCreatedAt());
    }

    @Test
    void testNoArgsConstructor() {
        // When
        Reply replyEntity = new Reply();

        // Then
        assertNotNull(replyEntity);
        assertNull(replyEntity.getId());
        assertNull(replyEntity.getUserId());
        assertNull(replyEntity.getPostId());
        assertNull(replyEntity.getContent());
        assertNull(replyEntity.getCreatedAt());
    }

    @Test
    void testToString() {
        // Given
        reply.setId(1L);
        reply.setUserId(100L);
        reply.setPostId(200L);
        reply.setContent("Test content");

        // When
        String toStringResult = reply.toString();

        // Then
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("Reply"));
        assertTrue(toStringResult.contains("id=1"));
        assertTrue(toStringResult.contains("userId=100"));
        assertTrue(toStringResult.contains("postId=200"));
        assertTrue(toStringResult.contains("content=Test content"));
    }

    @Test
    void testEquals_SameObject() {
        // When & Then
        assertEquals(reply, reply);
    }

    @Test
    void testEquals_EqualObjects() {
        // Given
        Reply other = new Reply();
        other.setId(1L);
        reply.setId(1L);

        // When & Then
        assertEquals(reply, other);
    }

    @Test
    void testEquals_DifferentObjects() {
        // Given
        Reply other = new Reply();
        other.setId(1L);
        reply.setId(2L);

        // When & Then
        assertNotEquals(reply, other);
    }

    @Test
    void testEquals_NullObject() {
        // When & Then
        assertNotEquals(null, reply);
    }

    @Test
    void testEquals_DifferentClass() {
        // Given
        String other = "other";

        // When & Then
        assertNotEquals(reply, other);
    }

    @Test
    void testHashCode_SameObject() {
        // When
        int hashCode1 = reply.hashCode();
        int hashCode2 = reply.hashCode();

        // Then
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testHashCode_EqualObjects() {
        // Given
        Reply other = new Reply();
        other.setId(1L);
        reply.setId(1L);

        // When
        int hashCode1 = reply.hashCode();
        int hashCode2 = other.hashCode();

        // Then
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testAllFieldsTogether() {
        // Given
        Long id = 1L;
        Long userId = 100L;
        Long postId = 200L;
        String content = "Test reply content";
        LocalDateTime createdAt = LocalDateTime.of(2023, 1, 1, 12, 0, 0);

        // When
        reply.setId(id);
        reply.setUserId(userId);
        reply.setPostId(postId);
        reply.setContent(content);
        reply.setCreatedAt(createdAt);

        // Then
        assertEquals(id, reply.getId());
        assertEquals(userId, reply.getUserId());
        assertEquals(postId, reply.getPostId());
        assertEquals(content, reply.getContent());
        assertEquals(createdAt, reply.getCreatedAt());
    }
}
