// 创建 ReplyController.java
package com.example.appbacked.controller;

import com.example.appbacked.entity.Reply;
import com.example.appbacked.service.ReplyService;
import com.example.appbacked.service.PostService;
import com.example.appbacked.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/replies")
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @Autowired
    private PostService postService;

    @Autowired
    private ReplyRepository replyRepository; // 添加这行来注入 ReplyRepository


    /**
     * 获取帖子的所有回复
     */
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Reply>> getRepliesByPostId(@PathVariable Long postId) {
        List<Reply> replies = replyService.getRepliesByPostId(postId);
        return ResponseEntity.ok(replies);
    }

    /**
     * 添加回复
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> addReply(@RequestBody ReplyRequest request) {
        try {
            Reply reply = replyService.addReply(request.getPostId(), request.getUserId(), request.getContent());

            // 更新帖子的回复计数
            postService.updatePostReplyCount(request.getPostId());

            Map<String, Object> response = new HashMap<>();
            response.put("reply", reply);
            response.put("replyCount", replyService.getRepliesByPostId(request.getPostId()).size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "添加回复失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 删除回复
     */
    @DeleteMapping("/{replyId}")
    public ResponseEntity<Map<String, Object>> deleteReply(@PathVariable Long replyId) {
        try {
            Reply reply = replyService.getRepliesByPostId(replyId).stream().findFirst().orElse(null);
            if (reply == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "回复不存在");
                return ResponseEntity.badRequest().body(error);
            }

            replyService.deleteReply(replyId);

            // 更新帖子的回复计数
            postService.updatePostReplyCount(reply.getPostId());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "删除成功");
            response.put("replyCount", replyService.getRepliesByPostId(reply.getPostId()).size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "删除回复失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 回复点赞/取消点赞
     */
    @PostMapping("/{replyId}/like")
    public ResponseEntity<Map<String, Object>> toggleReplyLike(@PathVariable Long replyId, @RequestParam Long userId) {
        try {
            boolean isLiked = replyService.toggleReplyLike(replyId, userId);

            Map<String, Object> response = new HashMap<>();
            response.put("liked", isLiked);

            // 重新查询最新的数据以获取正确的计数
            Reply reply = replyRepository.findById(replyId).orElse(null);
            if (reply != null) {
                response.put("plikeCount", reply.getPlikeCount());
            } else {
                response.put("plikeCount", 0);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "操作失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }


    // 修改 ReplyController.java 中的 toggleReplyReport 方法
    /*@PostMapping("/{replyId}/report")
    public ResponseEntity<Map<String, Object>> toggleReplyReport(@PathVariable Long replyId,
                                                                 @RequestParam Long userId,
                                                                 @RequestParam String reason) {
        try {
            boolean isReported = replyService.toggleReplyReport(replyId, userId, reason);

            Map<String, Object> response = new HashMap<>();
            response.put("reported", isReported);

            // 使用正确的字段名 preportCount
            Reply reply = replyService.getRepliesByPostId(replyId).stream()
                    .findFirst().orElse(null);
            if (reply != null) {
                response.put("preportCount", reply.getPreportCount());
            } else {
                response.put("preportCount", 0);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "操作失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }*/
    @PostMapping("/{replyId}/report")
    public ResponseEntity<Map<String, Object>> toggleReplyReport(@PathVariable Long replyId,
                                                                 @RequestParam Long userId,
                                                                 @RequestParam String reason) {
        try {
            boolean isReported = replyService.toggleReplyReport(replyId, userId, reason);

            Map<String, Object> response = new HashMap<>();
            response.put("reported", isReported);

            // 重新查询最新的数据以获取正确的计数
            Reply reply = replyRepository.findById(replyId).orElse(null);
            if (reply != null) {
                response.put("preportCount", reply.getPreportCount());
            } else {
                response.put("preportCount", 0);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "操作失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 检查回复的点赞和举报状态
     */
    @GetMapping("/{replyId}/status")
    public ResponseEntity<Map<String, Object>> getReplyStatus(@PathVariable Long replyId, @RequestParam Long userId) {
        try {
            boolean isLiked = replyService.isReplyLikedByUser(replyId, userId);
            boolean isReported = replyService.isReplyReportedByUser(replyId, userId);

            Map<String, Object> response = new HashMap<>();
            response.put("liked", isLiked);
            response.put("reported", isReported);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "获取状态失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}
