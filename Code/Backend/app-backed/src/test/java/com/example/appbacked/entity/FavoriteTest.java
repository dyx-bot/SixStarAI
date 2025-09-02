package com.example.appbacked.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FavoriteTest {

    private Favorite favorite;

    @BeforeEach
    void setUp() {
        favorite = new Favorite();
    }

    @Test
    void testGetSetId() {
        // Given
        Long id = 1L;

        // When
        favorite.setId(id);

        // Then
        assertEquals(id, favorite.getId());
    }

    @Test
    void testGetSetUserId() {
        // Given
        Long userId = 100L;

        // When
        favorite.setUserId(userId);

        // Then
        assertEquals(userId, favorite.getUserId());
    }

    @Test
    void testGetSetPostId() {
        // Given
        Long postId = 200L;

        // When
        favorite.setPostId(postId);

        // Then
        assertEquals(postId, favorite.getPostId());
    }

    @Test
    void testGetSetCreatedAt() {
        // Given
        LocalDateTime createdAt = LocalDateTime.now();

        // When
        favorite.setCreatedAt(createdAt);

        // Then
        assertEquals(createdAt, favorite.getCreatedAt());
    }

    @Test
    void testGetSetCreatedAt_NullValue() {
        // When
        favorite.setCreatedAt(null);

        // Then
        assertNull(favorite.getCreatedAt());
    }

    @Test
    void testNoArgsConstructor() {
        // When
        Favorite favoriteEntity = new Favorite();

        // Then
        assertNotNull(favoriteEntity);
        assertNull(favoriteEntity.getId());
        assertNull(favoriteEntity.getUserId());
        assertNull(favoriteEntity.getPostId());
        assertNull(favoriteEntity.getCreatedAt());
    }

    @Test
    void testToString() {
        // Given
        favorite.setId(1L);
        favorite.setUserId(100L);
        favorite.setPostId(200L);

        // When
        String toStringResult = favorite.toString();

        // Then
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("Favorite"));
        assertTrue(toStringResult.contains("id=1"));
        assertTrue(toStringResult.contains("userId=100"));
        assertTrue(toStringResult.contains("postId=200"));
    }

    @Test
    void testEquals_SameObject() {
        // When & Then
        assertEquals(favorite, favorite);
    }

    @Test
    void testEquals_EqualObjects() {
        // Given
        Favorite other = new Favorite();
        other.setId(1L);
        favorite.setId(1L);

        // When & Then
        assertEquals(favorite, other);
    }

    @Test
    void testEquals_DifferentObjects() {
        // Given
        Favorite other = new Favorite();
        other.setId(1L);
        favorite.setId(2L);

        // When & Then
        assertNotEquals(favorite, other);
    }

    @Test
    void testEquals_NullObject() {
        // When & Then
        assertNotEquals(null, favorite);
    }

    @Test
    void testEquals_DifferentClass() {
        // Given
        String other = "other";

        // When & Then
        assertNotEquals(favorite, other);
    }

    @Test
    void testHashCode_SameObject() {
        // When
        int hashCode1 = favorite.hashCode();
        int hashCode2 = favorite.hashCode();

        // Then
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testHashCode_EqualObjects() {
        // Given
        Favorite other = new Favorite();
        other.setId(1L);
        favorite.setId(1L);

        // When
        int hashCode1 = favorite.hashCode();
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
        favorite.setId(id);
        favorite.setUserId(userId);
        favorite.setPostId(postId);
        favorite.setCreatedAt(createdAt);

        // Then
        assertEquals(id, favorite.getId());
        assertEquals(userId, favorite.getUserId());
        assertEquals(postId, favorite.getPostId());
        assertEquals(createdAt, favorite.getCreatedAt());
    }
}
