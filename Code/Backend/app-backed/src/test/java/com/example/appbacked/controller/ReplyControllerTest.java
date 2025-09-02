package com.example.appbacked.controller;

import com.example.appbacked.entity.Reply;
import com.example.appbacked.entity.Post;
import com.example.appbacked.repository.PostRepository;
import com.example.appbacked.repository.ReplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReplyControllerTest {

    @Mock
    private ReplyRepository replyRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private ReplyController replyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetRepliesByPostId() {
        // Given
        Long postId = 1L;
        List<Reply> replies = new ArrayList<>();
        Reply reply = new Reply();
        reply.setId(1L);
        reply.setPostId(postId);
        reply.setUserId(1L);
        reply.setContent("Test reply");
        reply.setCreatedAt(LocalDateTime.now());
        replies.add(reply);

        when(replyRepository.findByPostId(postId)).thenReturn(replies);

        // When
        ResponseEntity<List<Reply>> response = replyController.getRepliesByPostId(postId);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(reply.getId(), response.getBody().get(0).getId());
        verify(replyRepository, times(1)).findByPostId(postId);
    }

    @Test
    void testAddReply() {
        // Given
        ReplyRequest request = new ReplyRequest();
        request.setPostId(1L);
        request.setUserId(1L);
        request.setContent("Test reply");

        Reply savedReply = new Reply();
        savedReply.setId(1L);
        savedReply.setPostId(1L);
        savedReply.setUserId(1L);
        savedReply.setContent("Test reply");
        savedReply.setCreatedAt(LocalDateTime.now());

        Post post = new Post();
        post.setId(1L);
        post.setTitle("Test Post");
        post.setContent("Test Content");
        post.setReplyCount(0);

        when(replyRepository.save(any(Reply.class))).thenReturn(savedReply);
        when(replyRepository.countByPostId(1L)).thenReturn(1L);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        // When
        ResponseEntity<Map<String, Object>> response = replyController.addReply(request);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("reply"));
        assertTrue(response.getBody().containsKey("replyCount"));
        assertEquals(1L, response.getBody().get("replyCount"));

        verify(replyRepository, times(1)).save(any(Reply.class));
        // 修改为 times(2)，因为方法会被调用两次
        verify(replyRepository, times(2)).countByPostId(1L);
        verify(postRepository, times(1)).findById(1L);
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void testDeleteReply_Success() {
        // Given
        Long replyId = 1L;
        Reply reply = new Reply();
        reply.setId(replyId);
        reply.setPostId(1L);
        reply.setUserId(1L);
        reply.setContent("Test reply");
        reply.setCreatedAt(LocalDateTime.now());

        Post post = new Post();
        post.setId(1L);
        post.setTitle("Test Post");
        post.setContent("Test Content");
        post.setReplyCount(1);

        when(replyRepository.findById(replyId)).thenReturn(Optional.of(reply));
        // 修改为 times(2)，因为方法会被调用两次
        when(replyRepository.countByPostId(1L)).thenReturn(0L);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        // When
        ResponseEntity<Map<String, Object>> response = replyController.deleteReply(replyId);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("message"));
        assertTrue(response.getBody().containsKey("replyCount"));
        assertEquals("删除成功", response.getBody().get("message"));
        assertEquals(0L, response.getBody().get("replyCount"));

        verify(replyRepository, times(1)).findById(replyId);
        verify(replyRepository, times(1)).deleteById(replyId);
        // 修改为 times(2)，因为方法会被调用两次
        verify(replyRepository, times(2)).countByPostId(1L);
        verify(postRepository, times(1)).findById(1L);
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void testDeleteReply_NotFound() {
        // Given
        Long replyId = 1L;
        when(replyRepository.findById(replyId)).thenReturn(Optional.empty());

        // When
        ResponseEntity<Map<String, Object>> response = replyController.deleteReply(replyId);

        // Then
        assertEquals(400, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("error"));
        assertEquals("回复不存在", response.getBody().get("error"));

        verify(replyRepository, times(1)).findById(replyId);
        verify(replyRepository, times(0)).deleteById(replyId);
        verify(postRepository, times(0)).findById(anyLong());
        verify(postRepository, times(0)).save(any(Post.class));
    }
}
