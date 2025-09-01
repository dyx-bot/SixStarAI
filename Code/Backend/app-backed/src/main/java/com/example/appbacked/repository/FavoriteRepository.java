package com.example.appbacked.repository;

import com.example.appbacked.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByUserIdAndPostId(Long userId, Long postId);
    void deleteByUserIdAndPostId(Long userId, Long postId);
    Long countByPostId(Long postId);
    Long countByUserId(Long userId); // 统计用户收藏了多少帖子

    // 添加按帖子ID删除的方法
    void deleteByPostId(Long postId);

    // 统计用户发布的帖子被收藏的总数
    @Query("SELECT COUNT(f) FROM Favorite f WHERE f.postId IN (SELECT p.id FROM Post p WHERE p.userId = :userId)")
    Long countByPostUserId(@Param("userId") Long userId);

    List<Favorite> findByUserId(Long userId);
}
