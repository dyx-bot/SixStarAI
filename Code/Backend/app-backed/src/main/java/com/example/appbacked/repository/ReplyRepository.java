/*
package com.example.appbacked.repository;

import com.example.appbacked.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByPostId(Long postId);
    Long countByPostId(Long postId);

    // 添加按帖子ID删除的方法
    void deleteByPostId(Long postId);
}*/
// 创建 ReplyRepository.java
package com.example.appbacked.repository;

import com.example.appbacked.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByPostId(Long postId);

    Long countByPostId(Long postId);

    @Modifying
    @Transactional
    void deleteByPostId(Long postId);

    // 添加回复点赞相关方法
    @Query("SELECT COUNT(r) FROM Reply r WHERE r.id = :replyId AND r.plikeCount > 0")
    Long countLikesByReplyId(@Param("replyId") Long replyId);

    // 添加回复举报相关方法
    @Query("SELECT COUNT(r) FROM Reply r WHERE r.id = :replyId AND r.preportCount > 0")
    Long countReportsByReplyId(@Param("replyId") Long replyId);
}
