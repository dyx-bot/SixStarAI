package com.example.appbacked.repository;

import com.example.appbacked.entity.Favorite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FavoriteRepositoryTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFindByUserIdAndPostId() {
        // Given
        Long userId = 1L;
        Long postId = 2L;
        Favorite favorite = new Favorite();
        favorite.setId(1L);
        favorite.setUserId(userId);
        favorite.setPostId(postId);

        when(favoriteRepository.findByUserIdAndPostId(userId, postId)).thenReturn(Optional.of(favorite));

        // When
        Optional<Favorite> result = favoriteRepository.findByUserIdAndPostId(userId, postId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(favorite.getId(), result.get().getId());
        assertEquals(userId, result.get().getUserId());
        assertEquals(postId, result.get().getPostId());

        verify(favoriteRepository, times(1)).findByUserIdAndPostId(userId, postId);
    }

    @Test
    void testFindByUserIdAndPostId_NotFound() {
        // Given
        Long userId = 1L;
        Long postId = 2L;

        when(favoriteRepository.findByUserIdAndPostId(userId, postId)).thenReturn(Optional.empty());

        // When
        Optional<Favorite> result = favoriteRepository.findByUserIdAndPostId(userId, postId);

        // Then
        assertFalse(result.isPresent());

        verify(favoriteRepository, times(1)).findByUserIdAndPostId(userId, postId);
    }

    @Test
    void testDeleteByUserIdAndPostId() {
        // Given
        Long userId = 1L;
        Long postId = 2L;

        // When
        favoriteRepository.deleteByUserIdAndPostId(userId, postId);

        // Then
        verify(favoriteRepository, times(1)).deleteByUserIdAndPostId(userId, postId);
    }

    @Test
    void testCountByPostId() {
        // Given
        Long postId = 1L;
        Long count = 5L;

        when(favoriteRepository.countByPostId(postId)).thenReturn(count);

        // When
        Long result = favoriteRepository.countByPostId(postId);

        // Then
        assertEquals(count, result);

        verify(favoriteRepository, times(1)).countByPostId(postId);
    }

    @Test
    void testCountByUserId() {
        // Given
        Long userId = 1L;
        Long count = 3L;

        when(favoriteRepository.countByUserId(userId)).thenReturn(count);

        // When
        Long result = favoriteRepository.countByUserId(userId);

        // Then
        assertEquals(count, result);

        verify(favoriteRepository, times(1)).countByUserId(userId);
    }

    @Test
    void testDeleteByPostId() {
        // Given
        Long postId = 1L;

        // When
        favoriteRepository.deleteByPostId(postId);

        // Then
        verify(favoriteRepository, times(1)).deleteByPostId(postId);
    }

    @Test
    void testCountByPostUserId() {
        // Given
        Long userId = 1L;
        Long count = 10L;

        when(favoriteRepository.countByPostUserId(userId)).thenReturn(count);

        // When
        Long result = favoriteRepository.countByPostUserId(userId);

        // Then
        assertEquals(count, result);

        verify(favoriteRepository, times(1)).countByPostUserId(userId);
    }

    @Test
    void testFindByUserId() {
        // Given
        Long userId = 1L;
        Favorite favorite1 = new Favorite();
        favorite1.setId(1L);
        favorite1.setUserId(userId);
        favorite1.setPostId(1L);

        Favorite favorite2 = new Favorite();
        favorite2.setId(2L);
        favorite2.setUserId(userId);
        favorite2.setPostId(2L);

        List<Favorite> favorites = Arrays.asList(favorite1, favorite2);

        when(favoriteRepository.findByUserId(userId)).thenReturn(favorites);

        // When
        List<Favorite> result = favoriteRepository.findByUserId(userId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(favorite1.getId(), result.get(0).getId());
        assertEquals(favorite2.getId(), result.get(1).getId());

        verify(favoriteRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testFindByUserId_EmptyResult() {
        // Given
        Long userId = 1L;
        List<Favorite> favorites = Arrays.asList();

        when(favoriteRepository.findByUserId(userId)).thenReturn(favorites);

        // When
        List<Favorite> result = favoriteRepository.findByUserId(userId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(favoriteRepository, times(1)).findByUserId(userId);
    }

    // 继承自 JpaRepository 的方法测试示例
    @Test
    void testSave() {
        // Given
        Favorite favorite = new Favorite();
        favorite.setUserId(1L);
        favorite.setPostId(2L);
        favorite.setId(1L);

        when(favoriteRepository.save(favorite)).thenReturn(favorite);

        // When
        Favorite result = favoriteRepository.save(favorite);

        // Then
        assertNotNull(result);
        assertEquals(favorite.getId(), result.getId());

        verify(favoriteRepository, times(1)).save(favorite);
    }

    @Test
    void testFindById() {
        // Given
        Long id = 1L;
        Favorite favorite = new Favorite();
        favorite.setId(id);
        favorite.setUserId(1L);
        favorite.setPostId(2L);

        when(favoriteRepository.findById(id)).thenReturn(Optional.of(favorite));

        // When
        Optional<Favorite> result = favoriteRepository.findById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());

        verify(favoriteRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteById() {
        // Given
        Long id = 1L;

        // When
        favoriteRepository.deleteById(id);

        // Then
        verify(favoriteRepository, times(1)).deleteById(id);
    }
}
