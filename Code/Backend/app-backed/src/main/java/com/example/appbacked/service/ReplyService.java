package com.example.appbacked.service;

import com.example.appbacked.entity.Reply;
import com.example.appbacked.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ReplyService {
    private static final Logger logger = LoggerFactory.getLogger(ReplyService.class);

    // 在 ReplyService 中添加一个简单的内存状态跟踪（仅用于演示）
    private static final Set<String> userLikedReplies = ConcurrentHashMap.newKeySet();
    private static final Set<String> userReportedReplies = ConcurrentHashMap.newKeySet();

    @Autowired
    private ReplyRepository replyRepository;

    /**
     * 获取帖子的所有回复
     */
    @Transactional(readOnly = true)
    public List<Reply> getRepliesByPostId(Long postId) {
        return replyRepository.findByPostId(postId);
    }

    /**
     * 添加回复
     */
    @Transactional
    public Reply addReply(Long postId, Long userId, String content) {
        Reply reply = new Reply();
        reply.setPostId(postId);
        reply.setUserId(userId);
        reply.setContent(content);
        reply.setPlikeCount(0); // 初始化点赞计数
        reply.setPreportCount(0); // 初始化举报计数
        reply.setCreatedAt(LocalDateTime.now());
        reply.setUpdatedAt(LocalDateTime.now());
        return replyRepository.save(reply);
    }

    /**
     * 删除回复
     */
    @Transactional
    public void deleteReply(Long replyId) {
        replyRepository.deleteById(replyId);
    }

    /**
     * 回复点赞/取消点赞（支持取消功能）
     */
    @Transactional
    public boolean toggleReplyLike(Long replyId, Long userId) {
        Reply reply = replyRepository.findById(replyId).orElse(null);
        if (reply == null) {
            return false;
        }

        String key = replyId + ":" + userId;
        boolean alreadyLiked = userLikedReplies.contains(key);

        if (alreadyLiked) {
            // 取消点赞
            userLikedReplies.remove(key);
            reply.setPlikeCount(Math.max(0, reply.getPlikeCount() - 1));
            logger.info("用户 {} 取消了对回复 {} 的点赞，当前点赞数: {}", userId, replyId, reply.getPlikeCount());
        } else {
            // 添加点赞
            userLikedReplies.add(key);
            reply.setPlikeCount(reply.getPlikeCount() + 1);
            logger.info("用户 {} 对回复 {} 进行了点赞，当前点赞数: {}", userId, replyId, reply.getPlikeCount());
        }

        reply.setUpdatedAt(LocalDateTime.now());
        replyRepository.saveAndFlush(reply);
        return !alreadyLiked; // 返回当前状态
    }

    /**
     * 回复举报/取消举报（支持取消功能）
     */
    @Transactional
    public boolean toggleReplyReport(Long replyId, Long userId, String reason) {
        Reply reply = replyRepository.findById(replyId).orElse(null);
        if (reply == null) {
            return false;
        }

        String key = replyId + ":" + userId;
        boolean alreadyReported = userReportedReplies.contains(key);

        if (alreadyReported) {
            // 取消举报
            userReportedReplies.remove(key);
            reply.setPreportCount(Math.max(0, reply.getPreportCount() - 1));
            logger.info("用户 {} 取消了对回复 {} 的举报，当前举报数: {}", userId, replyId, reply.getPreportCount());
        } else {
            // 添加举报
            userReportedReplies.add(key);
            reply.setPreportCount(reply.getPreportCount() + 1);
            logger.info("用户 {} 对回复 {} 进行了举报，当前举报数: {}", userId, replyId, reply.getPreportCount());
        }

        reply.setUpdatedAt(LocalDateTime.now());
        replyRepository.saveAndFlush(reply);
        return !alreadyReported; // 返回当前状态
    }

    /**
     * 检查用户是否已点赞某个回复
     */
    @Transactional(readOnly = true)
    public boolean isReplyLikedByUser(Long replyId, Long userId) {
        String key = replyId + ":" + userId;
        return userLikedReplies.contains(key);
    }

    /**
     * 检查用户是否已举报某个回复
     */
    @Transactional(readOnly = true)
    public boolean isReplyReportedByUser(Long replyId, Long userId) {
        String key = replyId + ":" + userId;
        return userReportedReplies.contains(key);
    }
}
