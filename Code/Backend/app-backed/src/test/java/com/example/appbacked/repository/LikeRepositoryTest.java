package com.example.appbacked.repository;

import com.example.appbacked.entity.Like;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LikeRepositoryTest {

    @Mock
    private LikeRepository likeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFindByUserIdAndPostId() {
        // Given
        Long userId = 1L;
        Long postId = 2L;
        Like like = new Like();
        like.setId(1L);
        like.setUserId(userId);
        like.setPostId(postId);

        when(likeRepository.findByUserIdAndPostId(userId, postId)).thenReturn(Optional.of(like));

        // When
        Optional<Like> result = likeRepository.findByUserIdAndPostId(userId, postId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(like.getId(), result.get().getId());
        assertEquals(userId, result.get().getUserId());
        assertEquals(postId, result.get().getPostId());

        verify(likeRepository, times(1)).findByUserIdAndPostId(userId, postId);
    }

    @Test
    void testFindByUserIdAndPostId_NotFound() {
        // Given
        Long userId = 1L;
        Long postId = 2L;

        when(likeRepository.findByUserIdAndPostId(userId, postId)).thenReturn(Optional.empty());

        // When
        Optional<Like> result = likeRepository.findByUserIdAndPostId(userId, postId);

        // Then
        assertFalse(result.isPresent());

        verify(likeRepository, times(1)).findByUserIdAndPostId(userId, postId);
    }

    @Test
    void testDeleteByUserIdAndPostId() {
        // Given
        Long userId = 1L;
        Long postId = 2L;

        // When
        likeRepository.deleteByUserIdAndPostId(userId, postId);

        // Then
        verify(likeRepository, times(1)).deleteByUserIdAndPostId(userId, postId);
    }

    @Test
    void testCountByPostId() {
        // Given
        Long postId = 1L;
        Long count = 5L;

        when(likeRepository.countByPostId(postId)).thenReturn(count);

        // When
        Long result = likeRepository.countByPostId(postId);

        // Then
        assertEquals(count, result);

        verify(likeRepository, times(1)).countByPostId(postId);
    }

    @Test
    void testCountByUserId() {
        // Given
        Long userId = 1L;
        Long count = 3L;

        when(likeRepository.countByUserId(userId)).thenReturn(count);

        // When
        Long result = likeRepository.countByUserId(userId);

        // Then
        assertEquals(count, result);

        verify(likeRepository, times(1)).countByUserId(userId);
    }

    @Test
    void testDeleteByPostId() {
        // Given
        Long postId = 1L;

        // When
        likeRepository.deleteByPostId(postId);

        // Then
        verify(likeRepository, times(1)).deleteByPostId(postId);
    }

    @Test
    void testCountByPostUserId() {
        // Given
        Long userId = 1L;
        Long count = 10L;

        when(likeRepository.countByPostUserId(userId)).thenReturn(count);

        // When
        Long result = likeRepository.countByPostUserId(userId);

        // Then
        assertEquals(count, result);

        verify(likeRepository, times(1)).countByPostUserId(userId);
    }

    @Test
    void testFindByUserId() {
        // Given
        Long userId = 1L;
        Like like1 = new Like();
        like1.setId(1L);
        like1.setUserId(userId);
        like1.setPostId(1L);

        Like like2 = new Like();
        like2.setId(2L);
        like2.setUserId(userId);
        like2.setPostId(2L);

        List<Like> likes = Arrays.asList(like1, like2);

        when(likeRepository.findByUserId(userId)).thenReturn(likes);

        // When
        List<Like> result = likeRepository.findByUserId(userId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(like1.getId(), result.get(0).getId());
        assertEquals(like2.getId(), result.get(1).getId());

        verify(likeRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testFindByUserId_EmptyResult() {
        // Given
        Long userId = 1L;
        List<Like> likes = Arrays.asList();

        when(likeRepository.findByUserId(userId)).thenReturn(likes);

        // When
        List<Like> result = likeRepository.findByUserId(userId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(likeRepository, times(1)).findByUserId(userId);
    }

    // 继承自 JpaRepository 的方法测试示例
    @Test
    void testSave() {
        // Given
        Like like = new Like();
        like.setUserId(1L);
        like.setPostId(2L);
        like.setId(1L);

        when(likeRepository.save(like)).thenReturn(like);

        // When
        Like result = likeRepository.save(like);

        // Then
        assertNotNull(result);
        assertEquals(like.getId(), result.getId());

        verify(likeRepository, times(1)).save(like);
    }

    @Test
    void testFindById() {
        // Given
        Long id = 1L;
        Like like = new Like();
        like.setId(id);
        like.setUserId(1L);
        like.setPostId(2L);

        when(likeRepository.findById(id)).thenReturn(Optional.of(like));

        // When
        Optional<Like> result = likeRepository.findById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());

        verify(likeRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteById() {
        // Given
        Long id = 1L;

        // When
        likeRepository.deleteById(id);

        // Then
        verify(likeRepository, times(1)).deleteById(id);
    }
}
