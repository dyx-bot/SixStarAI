package com.example.appbacked.repository;

import com.example.appbacked.entity.Reply;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReplyRepositoryTest {

    @Mock
    private ReplyRepository replyRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFindByPostId() {
        // Given
        Long postId = 1L;
        Reply reply1 = new Reply();
        reply1.setId(1L);
        reply1.setPostId(postId);
        reply1.setUserId(100L);
        reply1.setContent("Reply 1");
        reply1.setCreatedAt(LocalDateTime.now());

        Reply reply2 = new Reply();
        reply2.setId(2L);
        reply2.setPostId(postId);
        reply2.setUserId(101L);
        reply2.setContent("Reply 2");
        reply2.setCreatedAt(LocalDateTime.now());

        List<Reply> replies = Arrays.asList(reply1, reply2);

        when(replyRepository.findByPostId(postId)).thenReturn(replies);

        // When
        List<Reply> result = replyRepository.findByPostId(postId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(reply1.getId(), result.get(0).getId());
        assertEquals(reply2.getId(), result.get(1).getId());
        assertEquals(postId, result.get(0).getPostId());
        assertEquals(postId, result.get(1).getPostId());

        verify(replyRepository, times(1)).findByPostId(postId);
    }

    @Test
    void testFindByPostId_EmptyResult() {
        // Given
        Long postId = 1L;
        List<Reply> replies = Arrays.asList();

        when(replyRepository.findByPostId(postId)).thenReturn(replies);

        // When
        List<Reply> result = replyRepository.findByPostId(postId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(replyRepository, times(1)).findByPostId(postId);
    }

    @Test
    void testCountByPostId() {
        // Given
        Long postId = 1L;
        Long count = 5L;

        when(replyRepository.countByPostId(postId)).thenReturn(count);

        // When
        Long result = replyRepository.countByPostId(postId);

        // Then
        assertEquals(count, result);

        verify(replyRepository, times(1)).countByPostId(postId);
    }

    @Test
    void testCountByPostId_ZeroCount() {
        // Given
        Long postId = 1L;
        Long count = 0L;

        when(replyRepository.countByPostId(postId)).thenReturn(count);

        // When
        Long result = replyRepository.countByPostId(postId);

        // Then
        assertEquals(count, result);

        verify(replyRepository, times(1)).countByPostId(postId);
    }

    @Test
    void testDeleteByPostId() {
        // Given
        Long postId = 1L;

        // When
        replyRepository.deleteByPostId(postId);

        // Then
        verify(replyRepository, times(1)).deleteByPostId(postId);
    }

    // 继承自 JpaRepository 的方法测试示例
    @Test
    void testSave() {
        // Given
        Reply reply = new Reply();
        reply.setPostId(1L);
        reply.setUserId(100L);
        reply.setContent("Test Reply");
        reply.setId(1L);

        when(replyRepository.save(reply)).thenReturn(reply);

        // When
        Reply result = replyRepository.save(reply);

        // Then
        assertNotNull(result);
        assertEquals(reply.getId(), result.getId());
        assertEquals(reply.getContent(), result.getContent());

        verify(replyRepository, times(1)).save(reply);
    }

    @Test
    void testFindById() {
        // Given
        Long id = 1L;
        Reply reply = new Reply();
        reply.setId(id);
        reply.setPostId(1L);
        reply.setUserId(100L);
        reply.setContent("Test Reply");

        when(replyRepository.findById(id)).thenReturn(Optional.of(reply));

        // When
        Optional<Reply> result = replyRepository.findById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        assertEquals(reply.getContent(), result.get().getContent());

        verify(replyRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteById() {
        // Given
        Long id = 1L;

        // When
        replyRepository.deleteById(id);

        // Then
        verify(replyRepository, times(1)).deleteById(id);
    }

    @Test
    void testDelete() {
        // Given
        Reply reply = new Reply();
        reply.setId(1L);
        reply.setPostId(1L);
        reply.setUserId(100L);
        reply.setContent("Test Reply");

        // When
        replyRepository.delete(reply);

        // Then
        verify(replyRepository, times(1)).delete(reply);
    }
}
