package com.example.appbacked.controller;

import com.example.appbacked.entity.Favorite;
import com.example.appbacked.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteRepository favoriteRepository;

    // 收藏或取消收藏
    @PostMapping
    public ResponseEntity<Map<String, Object>> toggleFavorite(@RequestBody ToggleFavoriteRequest request) {
        Long userId = request.getUserId();
        Long postId = request.getPostId();

        Map<String, Object> response = new HashMap<>();

        // 检查是否已收藏
        var existingFavorite = favoriteRepository.findByUserIdAndPostId(userId, postId);

        if (existingFavorite.isPresent()) {
            // 已收藏，取消收藏
            favoriteRepository.delete(existingFavorite.get());
            response.put("action", "unfavorited");
        } else {
            // 未收藏，执行收藏
            Favorite favorite = new Favorite();
            favorite.setUserId(userId);
            favorite.setPostId(postId);
            favorite.setCreatedAt(LocalDateTime.now());
            favoriteRepository.save(favorite);
            response.put("action", "favorited");
        }

        // 返回最新的收藏数和状态
        Long favoriteCount = favoriteRepository.countByPostId(postId);
        response.put("favoriteCount", favoriteCount);
        response.put("isFavorited", !existingFavorite.isPresent());

        return ResponseEntity.ok(response);
    }
}

// DTO for favorite request
class ToggleFavoriteRequest {
    private Long userId;
    private Long postId;
    // getters and setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
}