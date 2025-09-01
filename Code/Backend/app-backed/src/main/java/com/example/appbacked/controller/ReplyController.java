package com.example.appbacked.controller;

import com.example.appbacked.entity.Reply;
import com.example.appbacked.repository.ReplyRepository;
import com.example.appbacked.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/replies")
public class ReplyController {

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private PostRepository postRepository;

    // 获取某个帖子的所有回复
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Reply>> getRepliesByPostId(@PathVariable Long postId) {
        List<Reply> replies = replyRepository.findByPostId(postId);
        return ResponseEntity.ok(replies);
    }

    // 添加回复
    @PostMapping
    public ResponseEntity<Map<String, Object>> addReply(@RequestBody ReplyRequest request) {
        Reply reply = new Reply();
        reply.setPostId(request.getPostId());
        reply.setUserId(request.getUserId());
        reply.setContent(request.getContent());
        reply.setCreatedAt(LocalDateTime.now());

        Reply savedReply = replyRepository.save(reply);

        // 更新帖子的回复数
        updatePostReplyCount(request.getPostId());

        Map<String, Object> response = new HashMap<>();
        response.put("reply", savedReply);

        // 返回最新的回复数
        Long replyCount = replyRepository.countByPostId(request.getPostId());
        response.put("replyCount", replyCount);

        return ResponseEntity.ok(response);
    }

    // 删除回复
    @DeleteMapping("/{replyId}")
    public ResponseEntity<Map<String, Object>> deleteReply(@PathVariable Long replyId) {
        Reply reply = replyRepository.findById(replyId).orElse(null);
        if (reply == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "回复不存在");
            return ResponseEntity.badRequest().body(response);
        }

        Long postId = reply.getPostId();
        replyRepository.deleteById(replyId);

        // 更新帖子的回复数
        updatePostReplyCount(postId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "删除成功");

        // 返回最新的回复数
        Long replyCount = replyRepository.countByPostId(postId);
        response.put("replyCount", replyCount);

        return ResponseEntity.ok(response);
    }

    private void updatePostReplyCount(Long postId) {
        postRepository.findById(postId).ifPresent(post -> {
            Long replyCount = replyRepository.countByPostId(postId);
            post.setReplyCount(replyCount.intValue());
            postRepository.save(post);
        });
    }
}

// DTO for reply request
class ReplyRequest {
    private Long postId;
    private Long userId;
    private String content;

    // getters and setters
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
