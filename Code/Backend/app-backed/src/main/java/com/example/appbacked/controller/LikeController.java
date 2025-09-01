package com.example.appbacked.controller;

import com.example.appbacked.entity.Like;
import com.example.appbacked.repository.LikeRepository;
import com.example.appbacked.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostRepository postRepository;

    // 点赞或取消点赞
    @PostMapping
    public ResponseEntity<Map<String, Object>> toggleLike(@RequestBody ToggleLikeRequest request) {
        Long userId = request.getUserId();
        Long postId = request.getPostId();

        Map<String, Object> response = new HashMap<>();

        // 检查是否已点赞
        var existingLike = likeRepository.findByUserIdAndPostId(userId, postId);

        if (existingLike.isPresent()) {
            // 已点赞，执行取消点赞
            likeRepository.delete(existingLike.get());
            response.put("action", "unliked");
        } else {
            // 未点赞，执行点赞
            Like like = new Like();
            like.setUserId(userId);
            like.setPostId(postId);
            like.setCreatedAt(LocalDateTime.now());
            likeRepository.save(like);
            response.put("action", "liked");
        }

        // 返回最新的点赞数和状态
        Long likeCount = likeRepository.countByPostId(postId);
        response.put("likeCount", likeCount);
        response.put("isLiked", !existingLike.isPresent()); // 点赞后为 true，取消后为 false

        // 更新帖子的点赞数
        updatePostLikeCount(postId);

        return ResponseEntity.ok(response);
    }

    private void updatePostLikeCount(Long postId) {
        postRepository.findById(postId).ifPresent(post -> {
            Long likeCount = likeRepository.countByPostId(postId);
            post.setLikeCount(likeCount.intValue());
            postRepository.save(post);
        });
    }
}

// DTO for like request
class ToggleLikeRequest {
    private Long userId;
    private Long postId;
    // getters and setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
}
