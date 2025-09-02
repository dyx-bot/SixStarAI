package com.example.appbacked.service;

import com.example.appbacked.dto.PostDetailDTO;
import com.example.appbacked.entity.Post;
import com.example.appbacked.entity.User;
import com.example.appbacked.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @InjectMocks
    private PostService postService;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPosts() {
        // 准备测试数据
        Long currentUserId = 1001L;

        Post post1 = new Post();
        post1.setId(1L);
        post1.setUserId(1002L);
        post1.setTitle("测试帖子1");
        post1.setContent("测试内容1");
        post1.setLikeCount(10);
        post1.setReplyCount(5);
        post1.setCreatedAt(LocalDateTime.now());

        Post post2 = new Post();
        post2.setId(2L);
        post2.setUserId(1003L);
        post2.setTitle("测试帖子2");
        post2.setContent("测试内容2");
        post2.setLikeCount(20);
        post2.setReplyCount(3);
        post2.setCreatedAt(LocalDateTime.now());

        List<Post> posts = new ArrayList<>();
        posts.add(post1);
        posts.add(post2);

        User user1 = new User();
        user1.setId(1002L);
        user1.setUsername("用户1");
        user1.setGrade("大一");
        user1.setCollege("计算机学院");

        User user2 = new User();
        user2.setId(1003L);
        user2.setUsername("用户2");
        user2.setGrade("大二");
        user2.setCollege("软件学院");

        // 设置模拟行为
        when(postRepository.findAll()).thenReturn(posts);
        when(userRepository.findById(1002L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(1003L)).thenReturn(Optional.of(user2));
        when(likeRepository.findByUserIdAndPostId(currentUserId, 1L)).thenReturn(Optional.empty());
        when(likeRepository.findByUserIdAndPostId(currentUserId, 2L)).thenReturn(Optional.empty());
        when(favoriteRepository.findByUserIdAndPostId(currentUserId, 1L)).thenReturn(Optional.empty());
        when(favoriteRepository.findByUserIdAndPostId(currentUserId, 2L)).thenReturn(Optional.empty());

        // 执行测试
        List<PostDetailDTO> result = postService.getAllPosts(currentUserId);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("测试帖子1", result.get(0).getTitle());
        assertEquals("测试帖子2", result.get(1).getTitle());
        assertEquals("用户1", result.get(0).getUsername());
        assertEquals("用户2", result.get(1).getUsername());

        // 验证方法调用
        verify(postRepository, times(1)).findAll();
        verify(userRepository, times(1)).findById(1002L);
        verify(userRepository, times(1)).findById(1003L);
    }

    @Test
    void testGetPostsByUserId() {
        // 准备测试数据
        Long userId = 1001L;

        Post post1 = new Post();
        post1.setId(1L);
        post1.setUserId(userId);
        post1.setTitle("我的帖子1");
        post1.setContent("我的内容1");
        post1.setLikeCount(15);
        post1.setReplyCount(8);
        post1.setCreatedAt(LocalDateTime.now());

        Post post2 = new Post();
        post2.setId(2L);
        post2.setUserId(userId);
        post2.setTitle("我的帖子2");
        post2.setContent("我的内容2");
        post2.setLikeCount(25);
        post2.setReplyCount(12);
        post2.setCreatedAt(LocalDateTime.now());

        List<Post> posts = new ArrayList<>();
        posts.add(post1);
        posts.add(post2);

        User user = new User();
        user.setId(userId);
        user.setUsername("测试用户");
        user.setGrade("大三");
        user.setCollege("信息学院");

        // 设置模拟行为
        when(postRepository.findByUserId(userId)).thenReturn(posts);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(likeRepository.findByUserIdAndPostId(userId, 1L)).thenReturn(Optional.empty());
        when(likeRepository.findByUserIdAndPostId(userId, 2L)).thenReturn(Optional.empty());
        when(favoriteRepository.findByUserIdAndPostId(userId, 1L)).thenReturn(Optional.empty());
        when(favoriteRepository.findByUserIdAndPostId(userId, 2L)).thenReturn(Optional.empty());

        // 执行测试
        List<PostDetailDTO> result = postService.getPostsByUserId(userId);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("我的帖子1", result.get(0).getTitle());
        assertEquals("我的帖子2", result.get(1).getTitle());
        assertEquals("测试用户", result.get(0).getUsername());
        assertEquals("测试用户", result.get(1).getUsername());

        // 验证方法调用 - 使用 atLeastOnce() 而不是 times(1)
        verify(postRepository, times(1)).findByUserId(userId);
        verify(userRepository, atLeastOnce()).findById(userId);
    }

    @Test
    void testGetFavoritePostsByUserId() {
        // 准备测试数据
        Long userId = 1001L;

        Post post1 = new Post();
        post1.setId(1L);
        post1.setUserId(1002L);
        post1.setTitle("收藏的帖子1");
        post1.setContent("收藏的内容1");
        post1.setLikeCount(30);
        post1.setReplyCount(15);
        post1.setCreatedAt(LocalDateTime.now());

        List<Post> posts = new ArrayList<>();
        posts.add(post1);

        User user = new User();
        user.setId(1002L);
        user.setUsername("帖子作者");
        user.setGrade("研一");
        user.setCollege("研究生院");

        // 设置模拟行为
        when(postRepository.findPostsByFavoriteUserId(userId)).thenReturn(posts);
        when(userRepository.findById(1002L)).thenReturn(Optional.of(user));
        when(likeRepository.findByUserIdAndPostId(userId, 1L)).thenReturn(Optional.empty());
        when(favoriteRepository.findByUserIdAndPostId(userId, 1L)).thenReturn(Optional.empty());

        // 执行测试
        List<PostDetailDTO> result = postService.getFavoritePostsByUserId(userId);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("收藏的帖子1", result.get(0).getTitle());
        assertEquals("帖子作者", result.get(0).getUsername());

        // 验证方法调用
        verify(postRepository, times(1)).findPostsByFavoriteUserId(userId);
        verify(userRepository, times(1)).findById(1002L);
    }

    @Test
    void testGetLikedPostsByUserId() {
        // 准备测试数据
        Long userId = 1001L;

        Post post1 = new Post();
        post1.setId(1L);
        post1.setUserId(1003L);
        post1.setTitle("点赞的帖子1");
        post1.setContent("点赞的内容1");
        post1.setLikeCount(40);
        post1.setReplyCount(20);
        post1.setCreatedAt(LocalDateTime.now());

        List<Post> posts = new ArrayList<>();
        posts.add(post1);

        User user = new User();
        user.setId(1003L);
        user.setUsername("被点赞用户");
        user.setGrade("大四");
        user.setCollege("电子工程学院");

        // 设置模拟行为
        when(postRepository.findPostsByLikeUserId(userId)).thenReturn(posts);
        when(userRepository.findById(1003L)).thenReturn(Optional.of(user));
        when(likeRepository.findByUserIdAndPostId(userId, 1L)).thenReturn(Optional.empty());
        when(favoriteRepository.findByUserIdAndPostId(userId, 1L)).thenReturn(Optional.empty());

        // 执行测试
        List<PostDetailDTO> result = postService.getLikedPostsByUserId(userId);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("点赞的帖子1", result.get(0).getTitle());
        assertEquals("被点赞用户", result.get(0).getUsername());

        // 验证方法调用
        verify(postRepository, times(1)).findPostsByLikeUserId(userId);
        verify(userRepository, times(1)).findById(1003L);
    }

    @Test
    void testUpdatePostLikeCount() {
        // 准备测试数据
        Long postId = 1L;

        Post post = new Post();
        post.setId(postId);
        post.setUserId(1001L);
        post.setTitle("测试帖子");
        post.setLikeCount(5);

        // 设置模拟行为
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(likeRepository.countByPostId(postId)).thenReturn(10L);
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // 执行测试
        postService.updatePostLikeCount(postId);

        // 验证结果
        assertEquals(10, post.getLikeCount());

        // 验证方法调用
        verify(postRepository, times(1)).findById(postId);
        verify(likeRepository, times(1)).countByPostId(postId);
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void testUpdatePostReplyCount() {
        // 准备测试数据
        Long postId = 1L;

        Post post = new Post();
        post.setId(postId);
        post.setUserId(1001L);
        post.setTitle("测试帖子");
        post.setReplyCount(3);

        // 设置模拟行为
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(replyRepository.countByPostId(postId)).thenReturn(8L);
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // 执行测试
        postService.updatePostReplyCount(postId);

        // 验证结果
        assertEquals(8, post.getReplyCount());

        // 验证方法调用
        verify(postRepository, times(1)).findById(postId);
        verify(replyRepository, times(1)).countByPostId(postId);
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void testDeletePostSuccess() {
        // 准备测试数据
        Long postId = 1L;
        Long userId = 1001L;

        Post post = new Post();
        post.setId(postId);
        post.setUserId(userId);
        post.setTitle("要删除的帖子");

        // 设置模拟行为
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // 执行测试
        assertDoesNotThrow(() -> postService.deletePost(postId, userId));

        // 验证方法调用
        verify(postRepository, times(1)).findById(postId);
        verify(likeRepository, times(1)).deleteByPostId(postId);
        verify(favoriteRepository, times(1)).deleteByPostId(postId);
        verify(replyRepository, times(1)).deleteByPostId(postId);
        verify(postRepository, times(1)).deleteById(postId);
    }

    @Test
    void testDeletePostNotFound() {
        // 准备测试数据
        Long postId = 999L;
        Long userId = 1001L;

        // 设置模拟行为
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.deletePost(postId, userId);
        });

        assertEquals("帖子不存在", exception.getMessage());

        // 验证方法调用
        verify(postRepository, times(1)).findById(postId);
        verify(likeRepository, never()).deleteByPostId(anyLong());
        verify(favoriteRepository, never()).deleteByPostId(anyLong());
        verify(replyRepository, never()).deleteByPostId(anyLong());
        verify(postRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeletePostNotOwner() {
        // 准备测试数据
        Long postId = 1L;
        Long userId = 1001L;
        Long ownerId = 1002L;

        Post post = new Post();
        post.setId(postId);
        post.setUserId(ownerId);
        post.setTitle("别人的帖子");

        // 设置模拟行为
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.deletePost(postId, userId);
        });

        assertEquals("只能删除自己发布的帖子", exception.getMessage());

        // 验证方法调用
        verify(postRepository, times(1)).findById(postId);
        verify(likeRepository, never()).deleteByPostId(anyLong());
        verify(favoriteRepository, never()).deleteByPostId(anyLong());
        verify(replyRepository, never()).deleteByPostId(anyLong());
        verify(postRepository, never()).deleteById(anyLong());
    }
}
