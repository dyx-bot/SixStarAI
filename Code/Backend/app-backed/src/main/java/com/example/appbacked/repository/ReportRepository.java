// ReportRepository.java
package com.example.appbacked.repository;

import com.example.appbacked.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    boolean existsByPostIdAndUserId(Long postId, Long userId);
}
