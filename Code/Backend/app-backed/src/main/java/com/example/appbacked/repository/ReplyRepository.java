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
}
