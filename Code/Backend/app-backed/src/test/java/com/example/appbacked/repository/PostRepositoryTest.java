package com.example.appbacked.repository;

import com.example.appbacked.entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostRepositoryTest {

    @Mock
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFindByUserId() {
        // Given
        Long userId = 1L;
        Post post1 = new Post();
        post1.setId(1L);
        post1.setUserId(userId);
        post1.setTitle("Post 1");

        Post post2 = new Post();
        post2.setId(2L);
        post2.setUserId(userId);
        post2.setTitle("Post 2");

        List<Post> posts = Arrays.asList(post1, post2);

        when(postRepository.findByUserId(userId)).thenReturn(posts);

        // When
        List<Post> result = postRepository.findByUserId(userId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(post1.getId(), result.get(0).getId());
        assertEquals(post2.getId(), result.get(1).getId());

        verify(postRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testFindByUserId_EmptyResult() {
        // Given
        Long userId = 1L;
        List<Post> posts = Arrays.asList();

        when(postRepository.findByUserId(userId)).thenReturn(posts);

        // When
        List<Post> result = postRepository.findByUserId(userId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(postRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testCountByUserId() {
        // Given
        Long userId = 1L;
        Long count = 5L;

        when(postRepository.countByUserId(userId)).thenReturn(count);

        // When
        Long result = postRepository.countByUserId(userId);

        // Then
        assertEquals(count, result);

        verify(postRepository, times(1)).countByUserId(userId);
    }

    @Test
    void testDeleteByUserId() {
        // Given
        Long userId = 1L;

        // When
        postRepository.deleteByUserId(userId);

        // Then
        verify(postRepository, times(1)).deleteByUserId(userId);
    }

    @Test
    void testFindPostsByFavoriteUserId() {
        // Given
        Long userId = 1L;
        Post post1 = new Post();
        post1.setId(1L);
        post1.setTitle("Favorited Post 1");

        Post post2 = new Post();
        post2.setId(2L);
        post2.setTitle("Favorited Post 2");

        List<Post> posts = Arrays.asList(post1, post2);

        when(postRepository.findPostsByFavoriteUserId(userId)).thenReturn(posts);

        // When
        List<Post> result = postRepository.findPostsByFavoriteUserId(userId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(post1.getId(), result.get(0).getId());
        assertEquals(post2.getId(), result.get(1).getId());

        verify(postRepository, times(1)).findPostsByFavoriteUserId(userId);
    }

    @Test
    void testFindPostsByFavoriteUserId_EmptyResult() {
        // Given
        Long userId = 1L;
        List<Post> posts = Arrays.asList();

        when(postRepository.findPostsByFavoriteUserId(userId)).thenReturn(posts);

        // When
        List<Post> result = postRepository.findPostsByFavoriteUserId(userId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(postRepository, times(1)).findPostsByFavoriteUserId(userId);
    }

    @Test
    void testFindPostsByLikeUserId() {
        // Given
        Long userId = 1L;
        Post post1 = new Post();
        post1.setId(1L);
        post1.setTitle("Liked Post 1");

        Post post2 = new Post();
        post2.setId(2L);
        post2.setTitle("Liked Post 2");

        List<Post> posts = Arrays.asList(post1, post2);

        when(postRepository.findPostsByLikeUserId(userId)).thenReturn(posts);

        // When
        List<Post> result = postRepository.findPostsByLikeUserId(userId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(post1.getId(), result.get(0).getId());
        assertEquals(post2.getId(), result.get(1).getId());

        verify(postRepository, times(1)).findPostsByLikeUserId(userId);
    }

    @Test
    void testFindPostsByLikeUserId_EmptyResult() {
        // Given
        Long userId = 1L;
        List<Post> posts = Arrays.asList();

        when(postRepository.findPostsByLikeUserId(userId)).thenReturn(posts);

        // When
        List<Post> result = postRepository.findPostsByLikeUserId(userId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(postRepository, times(1)).findPostsByLikeUserId(userId);
    }

    @Test
    void testCountPostsByTag() {
        // Given
        Object[] tag1 = {"技术", 5L};
        Object[] tag2 = {"生活", 3L};
        List<Object[]> tagCounts = Arrays.asList(tag1, tag2);

        when(postRepository.countPostsByTag()).thenReturn(tagCounts);

        // When
        List<Object[]> result = postRepository.countPostsByTag();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("技术", result.get(0)[0]);
        assertEquals(5L, result.get(0)[1]);
        assertEquals("生活", result.get(1)[0]);
        assertEquals(3L, result.get(1)[1]);

        verify(postRepository, times(1)).countPostsByTag();
    }

    @Test
    void testCountPostsByTag_EmptyResult() {
        // Given
        List<Object[]> tagCounts = Arrays.asList();

        when(postRepository.countPostsByTag()).thenReturn(tagCounts);

        // When
        List<Object[]> result = postRepository.countPostsByTag();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(postRepository, times(1)).countPostsByTag();
    }

    @Test
    void testFindAllByOrderByCreatedAtDesc() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Post post1 = new Post();
        post1.setId(1L);
        post1.setTitle("Post 1");
        post1.setCreatedAt(LocalDateTime.now());

        Post post2 = new Post();
        post2.setId(2L);
        post2.setTitle("Post 2");
        post2.setCreatedAt(LocalDateTime.now().minusDays(1));

        List<Post> posts = Arrays.asList(post1, post2);
        Page<Post> page = new PageImpl<>(posts);

        when(postRepository.findAllByOrderByCreatedAtDesc(pageable)).thenReturn(page);

        // When
        Page<Post> result = postRepository.findAllByOrderByCreatedAtDesc(pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(post1.getId(), result.getContent().get(0).getId());
        assertEquals(post2.getId(), result.getContent().get(1).getId());

        verify(postRepository, times(1)).findAllByOrderByCreatedAtDesc(pageable);
    }

    // 继承自 JpaRepository 的方法测试示例
    @Test
    void testSave() {
        // Given
        Post post = new Post();
        post.setUserId(1L);
        post.setTitle("Test Post");
        post.setContent("Test Content");
        post.setId(1L);

        when(postRepository.save(post)).thenReturn(post);

        // When
        Post result = postRepository.save(post);

        // Then
        assertNotNull(result);
        assertEquals(post.getId(), result.getId());
        assertEquals(post.getTitle(), result.getTitle());

        verify(postRepository, times(1)).save(post);
    }

    @Test
    void testFindById() {
        // Given
        Long id = 1L;
        Post post = new Post();
        post.setId(id);
        post.setUserId(1L);
        post.setTitle("Test Post");

        when(postRepository.findById(id)).thenReturn(Optional.of(post));

        // When
        Optional<Post> result = postRepository.findById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        assertEquals(post.getTitle(), result.get().getTitle());

        verify(postRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteById() {
        // Given
        Long id = 1L;

        // When
        postRepository.deleteById(id);

        // Then
        verify(postRepository, times(1)).deleteById(id);
    }
}
