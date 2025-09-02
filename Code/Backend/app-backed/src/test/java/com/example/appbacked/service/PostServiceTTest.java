package com.example.appbacked.service;

import com.example.appbacked.dto.PostDetailDTO;
import com.example.appbacked.entity.Post;
import com.example.appbacked.entity.User;
import com.example.appbacked.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private ReplyRepository replyRepository;

    @InjectMocks
    private PostService postService;

    private Post testPost;
    private User testUser;

    @BeforeEach
    void setUp() {
        testPost = new Post();
        testPost.setId(1L);
        testPost.setTitle("Test Post");
        testPost.setContent("Test Content");
        testPost.setUserId(1L);
        testPost.setIsHideCollege(false);
        testPost.setIsHideGrade(false);
        testPost.setTag("test");
        testPost.setLikeCount(10);
        testPost.setReplyCount(5);
        testPost.setCreatedAt(LocalDateTime.now());

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");
        testUser.setAvatar("testAvatar");
        testUser.setGrade("2021");
        testUser.setCollege("Computer Science");
    }

    @Test
    void getAllPostsShouldReturnPostDetailDTOs() {
        List<Post> posts = new ArrayList<>();
        posts.add(testPost);

        when(postRepository.findAll()).thenReturn(posts);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(likeRepository.findByUserIdAndPostId(2L, 1L)).thenReturn(Optional.empty());
        when(favoriteRepository.findByUserIdAndPostId(2L, 1L)).thenReturn(Optional.empty());

        List<PostDetailDTO> result = postService.getAllPosts(2L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Post", result.get(0).getTitle());
        assertEquals("Test Content", result.get(0).getContent());
        verify(postRepository).findAll();
    }

    @Test
    void getPostsByUserIdShouldReturnUserPosts() {
        List<Post> posts = new ArrayList<>();
        posts.add(testPost);

        when(postRepository.findByUserId(1L)).thenReturn(posts);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(likeRepository.findByUserIdAndPostId(1L, 1L)).thenReturn(Optional.empty());
        when(favoriteRepository.findByUserIdAndPostId(1L, 1L)).thenReturn(Optional.empty());

        List<PostDetailDTO> result = postService.getPostsByUserId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Post", result.get(0).getTitle());
        verify(postRepository).findByUserId(1L);
    }

    @Test
    void getFavoritePostsByUserIdShouldReturnFavoritePosts() {
        List<Post> posts = new ArrayList<>();
        posts.add(testPost);

        when(postRepository.findPostsByFavoriteUserId(1L)).thenReturn(posts);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(likeRepository.findByUserIdAndPostId(1L, 1L)).thenReturn(Optional.empty());
        when(favoriteRepository.findByUserIdAndPostId(1L, 1L)).thenReturn(Optional.empty());

        List<PostDetailDTO> result = postService.getFavoritePostsByUserId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(postRepository).findPostsByFavoriteUserId(1L);
    }

    @Test
    void getLikedPostsByUserIdShouldReturnLikedPosts() {
        List<Post> posts = new ArrayList<>();
        posts.add(testPost);

        when(postRepository.findPostsByLikeUserId(1L)).thenReturn(posts);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(likeRepository.findByUserIdAndPostId(1L, 1L)).thenReturn(Optional.empty());
        when(favoriteRepository.findByUserIdAndPostId(1L, 1L)).thenReturn(Optional.empty());

        List<PostDetailDTO> result = postService.getLikedPostsByUserId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(postRepository).findPostsByLikeUserId(1L);
    }

    @Test
    void convertToDTOShouldReturnPostDetailDTO() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(likeRepository.findByUserIdAndPostId(2L, 1L)).thenReturn(Optional.empty());
        when(favoriteRepository.findByUserIdAndPostId(2L, 1L)).thenReturn(Optional.empty());

        PostDetailDTO result = postService.convertToDTO(testPost, 2L);

        assertNotNull(result);
        assertEquals("Test Post", result.getTitle());
        assertEquals("Test Content", result.getContent());
        assertEquals("testUser", result.getUsername());
        assertEquals("testAvatar", result.getAvatar());
        assertEquals("2021", result.getGrade());
        assertEquals("Computer Science", result.getCollege());
        assertEquals(10, result.getLikeCount());
        assertEquals(5, result.getReplyCount());
        assertFalse(result.getIsLiked());
        assertFalse(result.getIsFavorited());
    }

    @Test
    void convertToDTOWithLikedAndFavoritedShouldSetFlags() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(likeRepository.findByUserIdAndPostId(2L, 1L)).thenReturn(Optional.of(new com.example.appbacked.entity.Like()));
        when(favoriteRepository.findByUserIdAndPostId(2L, 1L)).thenReturn(Optional.of(new com.example.appbacked.entity.Favorite()));

        PostDetailDTO result = postService.convertToDTO(testPost, 2L);

        assertNotNull(result);
        assertTrue(result.getIsLiked());
        assertTrue(result.getIsFavorited());
    }

    @Test
    void convertToDTOWithHiddenInfoShouldHideGradeAndCollege() {
        testPost.setIsHideGrade(true);
        testPost.setIsHideCollege(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(likeRepository.findByUserIdAndPostId(2L, 1L)).thenReturn(Optional.empty());
        when(favoriteRepository.findByUserIdAndPostId(2L, 1L)).thenReturn(Optional.empty());

        PostDetailDTO result = postService.convertToDTO(testPost, 2L);

        assertNotNull(result);
        assertNull(result.getGrade());
        assertNull(result.getCollege());
    }

    @Test
    void convertToDTOWithNonExistentUserShouldSetDefaultValues() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(likeRepository.findByUserIdAndPostId(2L, 1L)).thenReturn(Optional.empty());
        when(favoriteRepository.findByUserIdAndPostId(2L, 1L)).thenReturn(Optional.empty());

        PostDetailDTO result = postService.convertToDTO(testPost, 2L);

        assertNotNull(result);
        assertEquals("未知用户", result.getUsername());
        assertNull(result.getAvatar());
        assertNull(result.getGrade());
        assertNull(result.getCollege());
    }

    @Test
    void updatePostLikeCountShouldUpdateLikeCount() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(likeRepository.countByPostId(1L)).thenReturn(15L);
        when(postRepository.save(any(Post.class))).thenReturn(testPost);

        postService.updatePostLikeCount(1L);

        verify(postRepository).findById(1L);
        verify(likeRepository).countByPostId(1L);
        verify(postRepository).save(any(Post.class));
        assertEquals(15, testPost.getLikeCount());
    }

    @Test
    void updatePostLikeCountWithNonExistentPostShouldNotUpdate() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        postService.updatePostLikeCount(1L);

        verify(postRepository).findById(1L);
        verify(likeRepository, never()).countByPostId(1L);
        verify(postRepository, never()).save(any());
    }

    @Test
    void updatePostReplyCountShouldUpdateReplyCount() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(replyRepository.countByPostId(1L)).thenReturn(8L);
        when(postRepository.save(any(Post.class))).thenReturn(testPost);

        postService.updatePostReplyCount(1L);

        verify(postRepository).findById(1L);
        verify(replyRepository).countByPostId(1L);
        verify(postRepository).save(any(Post.class));
        assertEquals(8, testPost.getReplyCount());
    }

    @Test
    void updatePostReplyCountWithNonExistentPostShouldNotUpdate() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        postService.updatePostReplyCount(1L);

        verify(postRepository).findById(1L);
        verify(replyRepository, never()).countByPostId(1L);
        verify(postRepository, never()).save(any());
    }

    @Test
    void deletePostShouldDeletePostAndRelatedData() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        doNothing().when(likeRepository).deleteByPostId(1L);
        doNothing().when(favoriteRepository).deleteByPostId(1L);
        doNothing().when(replyRepository).deleteByPostId(1L);
        doNothing().when(postRepository).deleteById(1L);

        assertDoesNotThrow(() -> {
            postService.deletePost(1L, 1L);
        });

        verify(postRepository).findById(1L);
        verify(likeRepository).deleteByPostId(1L);
        verify(favoriteRepository).deleteByPostId(1L);
        verify(replyRepository).deleteByPostId(1L);
        verify(postRepository).deleteById(1L);
    }

    @Test
    void deletePostWithNonExistentPostShouldThrowException() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.deletePost(1L, 1L);
        });

        assertEquals("帖子不存在", exception.getMessage());
        verify(postRepository).findById(1L);
        verify(likeRepository, never()).deleteByPostId(anyLong());
        verify(favoriteRepository, never()).deleteByPostId(anyLong());
        verify(replyRepository, never()).deleteByPostId(anyLong());
        verify(postRepository, never()).deleteById(anyLong());
    }

    @Test
    void deletePostWithUnauthorizedUserShouldThrowException() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.deletePost(1L, 2L);
        });

        assertEquals("只能删除自己发布的帖子", exception.getMessage());
        verify(postRepository).findById(1L);
        verify(likeRepository, never()).deleteByPostId(anyLong());
        verify(favoriteRepository, never()).deleteByPostId(anyLong());
        verify(replyRepository, never()).deleteByPostId(anyLong());
        verify(postRepository, never()).deleteById(anyLong());
    }

    @Test
    void deletePostWithExceptionShouldThrowRuntimeException() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        doThrow(new RuntimeException("Database error")).when(likeRepository).deleteByPostId(1L);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            postService.deletePost(1L, 1L);
        });

        assertTrue(exception.getMessage().contains("删除帖子失败"));
        verify(postRepository).findById(1L);
    }
}
