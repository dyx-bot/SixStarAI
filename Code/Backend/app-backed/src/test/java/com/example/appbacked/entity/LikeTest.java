package com.example.appbacked.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LikeTest {

    private Like like;

    @BeforeEach
    void setUp() {
        like = new Like();
    }

    @Test
    void testGetSetId() {
        // Given
        Long id = 1L;

        // When
        like.setId(id);

        // Then
        assertEquals(id, like.getId());
    }

    @Test
    void testGetSetUserId() {
        // Given
        Long userId = 100L;

        // When
        like.setUserId(userId);

        // Then
        assertEquals(userId, like.getUserId());
    }

    @Test
    void testGetSetPostId() {
        // Given
        Long postId = 200L;

        // When
        like.setPostId(postId);

        // Then
        assertEquals(postId, like.getPostId());
    }

    @Test
    void testGetSetCreatedAt() {
        // Given
        LocalDateTime createdAt = LocalDateTime.now();

        // When
        like.setCreatedAt(createdAt);

        // Then
        assertEquals(createdAt, like.getCreatedAt());
    }

    @Test
    void testGetSetCreatedAt_NullValue() {
        // When
        like.setCreatedAt(null);

        // Then
        assertNull(like.getCreatedAt());
    }

    @Test
    void testNoArgsConstructor() {
        // When
        Like likeEntity = new Like();

        // Then
        assertNotNull(likeEntity);
        assertNull(likeEntity.getId());
        assertNull(likeEntity.getUserId());
        assertNull(likeEntity.getPostId());
        assertNull(likeEntity.getCreatedAt());
    }

    @Test
    void testToString() {
        // Given
        like.setId(1L);
        like.setUserId(100L);
        like.setPostId(200L);

        // When
        String toStringResult = like.toString();

        // Then
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("Like"));
        assertTrue(toStringResult.contains("id=1"));
        assertTrue(toStringResult.contains("userId=100"));
        assertTrue(toStringResult.contains("postId=200"));
    }

    @Test
    void testEquals_SameObject() {
        // When & Then
        assertEquals(like, like);
    }

    @Test
    void testEquals_EqualObjects() {
        // Given
        Like other = new Like();
        other.setId(1L);
        like.setId(1L);

        // When & Then
        assertEquals(like, other);
    }

    @Test
    void testEquals_DifferentObjects() {
        // Given
        Like other = new Like();
        other.setId(1L);
        like.setId(2L);

        // When & Then
        assertNotEquals(like, other);
    }

    @Test
    void testEquals_NullObject() {
        // When & Then
        assertNotEquals(null, like);
    }

    @Test
    void testEquals_DifferentClass() {
        // Given
        String other = "other";

        // When & Then
        assertNotEquals(like, other);
    }

    @Test
    void testHashCode_SameObject() {
        // When
        int hashCode1 = like.hashCode();
        int hashCode2 = like.hashCode();

        // Then
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testHashCode_EqualObjects() {
        // Given
        Like other = new Like();
        other.setId(1L);
        like.setId(1L);

        // When
        int hashCode1 = like.hashCode();
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
        LocalDateTime createdAt = LocalDateTime.of(2023, 1, 1, 12, 0, 0);

        // When
        like.setId(id);
        like.setUserId(userId);
        like.setPostId(postId);
        like.setCreatedAt(createdAt);

        // Then
        assertEquals(id, like.getId());
        assertEquals(userId, like.getUserId());
        assertEquals(postId, like.getPostId());
        assertEquals(createdAt, like.getCreatedAt());
    }
}
