package com.example.appbacked.repository;


import com.example.appbacked.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUserId(Long userId);
    Long countByUserId(Long userId);

    // 添加根据帖子ID删除的方法
    void deleteByUserId(Long userId);

    @Query("SELECT p FROM Post p WHERE p.id IN (SELECT f.postId FROM Favorite f WHERE f.userId = :userId)")
    List<Post> findPostsByFavoriteUserId(@Param("userId") Long userId);

    @Query("SELECT p FROM Post p WHERE p.id IN (SELECT l.postId FROM Like l WHERE l.userId = :userId)")
    List<Post> findPostsByLikeUserId(@Param("userId") Long userId);

    // 添加标签统计方法
    @Query("SELECT p.tag, COUNT(p) FROM Post p WHERE p.tag IS NOT NULL AND p.tag != '' GROUP BY p.tag")
    List<Object[]> countPostsByTag();

    // 按时间倒序获取所有帖子（支持分页）
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);


}