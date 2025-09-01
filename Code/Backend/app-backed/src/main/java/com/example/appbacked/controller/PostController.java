package com.example.appbacked.controller;

import com.example.appbacked.dto.PostDetailDTO;
import com.example.appbacked.entity.Post;
import com.example.appbacked.entity.User;
import com.example.appbacked.service.PostService;
import com.example.appbacked.dto.CreatePostRequest;
import com.example.appbacked.repository.PostRepository;
import com.example.appbacked.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    // 发布新帖子
    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody CreatePostRequest request) {
        // 验证用户是否存在
        if (!userRepository.existsById(request.getUserId())) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "用户不存在");
            return ResponseEntity.notFound().build(); // 改为 404
        }

        User user = userRepository.findById(request.getUserId()).orElse(null);

        Post post = new Post();
        post.setUserId(request.getUserId());
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setIsHideCollege(request.getIsHideCollege() != null ? request.getIsHideCollege() : false);
        post.setIsHideGrade(request.getIsHideGrade() != null ? request.getIsHideGrade() : false);
        post.setTag(request.getTag());

        // 从用户信息中获取年级和学院
        if (user != null) {
            post.setGrade(user.getGrade());
            post.setCollege(user.getCollege());
        }

        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        // 保存帖子（JPA 会自动填充生成的 id）
        Post savedPost = postRepository.save(post);

        // 转换为 DTO 并返回
        PostDetailDTO dto = postService.convertToDTO(savedPost, request.getUserId());
        return ResponseEntity.ok(dto);
    }

    // 获取所有帖子
    @GetMapping("/get")
    public ResponseEntity<List<PostDetailDTO>> getAllPosts(@RequestParam(required = false) Long userId) {
        List<PostDetailDTO> posts;
        if (userId != null) {
            // 如果提供了 userId，则获取该用户能看到的所有帖子
            posts = postService.getAllPosts(userId);
        } else {
            // 否则获取所有帖子（使用系统用户ID，比如0L）
            posts = postService.getAllPosts(0L);
        }
        return ResponseEntity.ok(posts);
    }

    // 获取单个帖子详情
    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id, @RequestParam Long userId) {
        // 验证用户是否存在
        if (!userRepository.existsById(userId)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "用户不存在");
            return ResponseEntity.badRequest().body(error);
        }

        Post post = postRepository.findById(id).orElse(null);
        if (post == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "帖子不存在");
            return ResponseEntity.notFound().build();
        }
        PostDetailDTO dto = postService.convertToDTO(post, userId);
        return ResponseEntity.ok(dto);
    }

    // ==================== 新增的独立接口 ====================

    // 获取指定用户发布的所有帖子
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPostsByUserId(@PathVariable Long userId,
                                              @RequestParam(required = false, defaultValue = "0") Long currentUserId) {
        // 验证用户是否存在
        if (!userRepository.existsById(userId)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "用户不存在");
            return ResponseEntity.notFound().build();
        }

        List<PostDetailDTO> posts = postService.getPostsByUserId(userId);
        return ResponseEntity.ok(posts);
    }

    // 获取指定用户点赞的所有帖子
    @GetMapping("/liked/{userId}")
    public ResponseEntity<?> getLikedPostsByUserId(@PathVariable Long userId,
                                                   @RequestParam(required = false, defaultValue = "0") Long currentUserId) {
        // 验证用户是否存在
        if (!userRepository.existsById(userId)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "用户不存在");
            return ResponseEntity.notFound().build();
        }

        List<PostDetailDTO> posts = postService.getLikedPostsByUserId(userId);
        return ResponseEntity.ok(posts);
    }

    // 获取指定用户收藏的所有帖子
    @GetMapping("/favorite/{userId}")
    public ResponseEntity<?> getFavoritePostsByUserId(@PathVariable Long userId,
                                                      @RequestParam(required = false, defaultValue = "0") Long currentUserId) {
        // 验证用户是否存在
        if (!userRepository.existsById(userId)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "用户不存在");
            return ResponseEntity.notFound().build();
        }

        List<PostDetailDTO> posts = postService.getFavoritePostsByUserId(userId);
        return ResponseEntity.ok(posts);
    }

    // 删除帖子（仅允许帖子作者删除）
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId, @RequestParam Long userId) {
        try {
            // 验证用户是否存在
            if (!userRepository.existsById(userId)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "用户不存在");
            return ResponseEntity.badRequest().body(error);
        }

        // 验证帖子是否存在
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "帖子不存在");
            return ResponseEntity.notFound().build();
        }

        // 验证是否为帖子作者
        if (!post.getUserId().equals(userId)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "只能删除自己发布的帖子");
            return ResponseEntity.badRequest().body(error);
        }

        // 删除帖子（PostService会处理相关的点赞、收藏、回复等数据）
        postService.deletePost(postId, userId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "帖子删除成功");
        return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "删除帖子失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}

