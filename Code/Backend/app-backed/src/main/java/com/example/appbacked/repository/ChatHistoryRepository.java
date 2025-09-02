
package com.example.appbacked.repository;
// ChatHistoryRepository.java

import com.example.appbacked.entity.ChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {
    List<ChatHistory> findBySessionIdOrderByCreatedAtAsc(String sessionId);
}
