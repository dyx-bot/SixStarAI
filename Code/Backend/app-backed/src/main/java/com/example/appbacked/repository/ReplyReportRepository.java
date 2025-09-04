// 创建 ReplyReportRepository.java
package com.example.appbacked.repository;

import com.example.appbacked.entity.ReplyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ReplyReportRepository extends JpaRepository<ReplyReport, Long> {
    Optional<ReplyReport> findByReplyIdAndUserId(@Param("replyId") Long replyId, @Param("userId") Long userId);

    Long countByReplyId(@Param("replyId") Long replyId);

    @Modifying
    @Transactional
    void deleteByReplyIdAndUserId(@Param("replyId") Long replyId, @Param("userId") Long userId);
}

