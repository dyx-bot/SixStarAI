package com.example.appbacked.repository;

import com.example.appbacked.entity.ChatHistory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DataAccessException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ChatHistoryRepositoryTest {

    @Autowired
    private ChatHistoryRepository chatHistoryRepository;

    @Test
    void chatHistoryRepositoryShouldBeLoaded() {
        assertNotNull(chatHistoryRepository);
    }

    @Test
    void chatHistoryRepositoryShouldExtendJpaRepository() {
        assertTrue(chatHistoryRepository instanceof org.springframework.data.jpa.repository.JpaRepository);
    }

    @Test
    void repositoryShouldSaveAndFindChatHistory() {
        // 创建测试数据
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.setSessionId("test-session");
        chatHistory.setMessage("Test message");
        chatHistory.setResponse("Test response");
        chatHistory.setCreatedAt(LocalDateTime.now());

        // 保存数据
        ChatHistory savedChatHistory = chatHistoryRepository.save(chatHistory);

        // 验证保存成功
        assertNotNull(savedChatHistory.getId());
        assertEquals("test-session", savedChatHistory.getSessionId());
        assertEquals("Test message", savedChatHistory.getMessage());
        assertEquals("Test response", savedChatHistory.getResponse());

        // 验证可以通过ID查找
        Optional<ChatHistory> foundChatHistory = chatHistoryRepository.findById(savedChatHistory.getId());
        assertTrue(foundChatHistory.isPresent());
        assertEquals(savedChatHistory.getId(), foundChatHistory.get().getId());
    }

    @Test
    void repositoryShouldFindAllChatHistories() {
        // 保存几条测试数据
        ChatHistory chatHistory1 = new ChatHistory();
        chatHistory1.setSessionId("test-session-1");
        chatHistory1.setMessage("Test message 1");
        chatHistory1.setResponse("Test response 1");
        chatHistory1.setCreatedAt(LocalDateTime.now());
        chatHistoryRepository.save(chatHistory1);

        ChatHistory chatHistory2 = new ChatHistory();
        chatHistory2.setSessionId("test-session-2");
        chatHistory2.setMessage("Test message 2");
        chatHistory2.setResponse("Test response 2");
        chatHistory2.setCreatedAt(LocalDateTime.now());
        chatHistoryRepository.save(chatHistory2);

        // 查找所有记录
        List<ChatHistory> allChatHistories = chatHistoryRepository.findAll();

        // 验证结果
        assertNotNull(allChatHistories);
        assertFalse(allChatHistories.isEmpty());
        assertTrue(allChatHistories.size() >= 2);
    }

    @Test
    void findBySessionIdOrderByCreatedAtAscShouldReturnOrderedResults() {
        String sessionId = "ordered-test-session";

        // 创建测试数据，注意时间顺序
        ChatHistory chatHistory1 = new ChatHistory();
        chatHistory1.setSessionId(sessionId);
        chatHistory1.setMessage("Message 1");
        chatHistory1.setResponse("Response 1");
        chatHistory1.setCreatedAt(LocalDateTime.of(2023, 1, 1, 10, 0));
        chatHistoryRepository.save(chatHistory1);

        ChatHistory chatHistory2 = new ChatHistory();
        chatHistory2.setSessionId(sessionId);
        chatHistory2.setMessage("Message 2");
        chatHistory2.setResponse("Response 2");
        chatHistory2.setCreatedAt(LocalDateTime.of(2023, 1, 1, 11, 0));
        chatHistoryRepository.save(chatHistory2);

        ChatHistory chatHistory3 = new ChatHistory();
        chatHistory3.setSessionId(sessionId);
        chatHistory3.setMessage("Message 3");
        chatHistory3.setResponse("Response 3");
        chatHistory3.setCreatedAt(LocalDateTime.of(2023, 1, 1, 12, 0));
        chatHistoryRepository.save(chatHistory3);

        // 创建另一个会话的数据，确保不会被查询到
        ChatHistory otherSessionChat = new ChatHistory();
        otherSessionChat.setSessionId("other-session");
        otherSessionChat.setMessage("Other message");
        otherSessionChat.setResponse("Other response");
        otherSessionChat.setCreatedAt(LocalDateTime.now());
        chatHistoryRepository.save(otherSessionChat);

        // 执行查询
        List<ChatHistory> result = chatHistoryRepository.findBySessionIdOrderByCreatedAtAsc(sessionId);

        // 验证结果
        assertNotNull(result);
        assertEquals(3, result.size());

        // 验证按时间升序排列
        assertTrue(result.get(0).getCreatedAt().isBefore(result.get(1).getCreatedAt()));
        assertTrue(result.get(1).getCreatedAt().isBefore(result.get(2).getCreatedAt()));

        // 验证所有结果都属于正确的会话
        for (ChatHistory chatHistory : result) {
            assertEquals(sessionId, chatHistory.getSessionId());
        }
    }

    @Test
    void findBySessionIdOrderByCreatedAtAscShouldReturnEmptyListWhenNoData() {
        String sessionId = "non-existent-session";

        List<ChatHistory> result = chatHistoryRepository.findBySessionIdOrderByCreatedAtAsc(sessionId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void repositoryShouldDeleteChatHistory() {
        // 创建并保存测试数据
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.setSessionId("delete-test-session");
        chatHistory.setMessage("Delete me");
        chatHistory.setResponse("To be deleted");
        chatHistory.setCreatedAt(LocalDateTime.now());

        ChatHistory savedChatHistory = chatHistoryRepository.save(chatHistory);
        Long id = savedChatHistory.getId();

        // 验证保存成功
        assertTrue(chatHistoryRepository.findById(id).isPresent());

        // 删除数据
        chatHistoryRepository.deleteById(id);

        // 验证删除成功
        assertFalse(chatHistoryRepository.findById(id).isPresent());
    }

    @Test
    void repositoryShouldUpdateChatHistory() {
        // 创建并保存测试数据
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.setSessionId("update-test-session");
        chatHistory.setMessage("Original message");
        chatHistory.setResponse("Original response");
        chatHistory.setCreatedAt(LocalDateTime.now());

        ChatHistory savedChatHistory = chatHistoryRepository.save(chatHistory);
        Long id = savedChatHistory.getId();

        // 更新数据
        savedChatHistory.setMessage("Updated message");
        savedChatHistory.setResponse("Updated response");
        ChatHistory updatedChatHistory = chatHistoryRepository.save(savedChatHistory);

        // 验证更新成功
        assertEquals("Updated message", updatedChatHistory.getMessage());
        assertEquals("Updated response", updatedChatHistory.getResponse());

        // 验证数据库中的数据也被更新
        Optional<ChatHistory> foundChatHistory = chatHistoryRepository.findById(id);
        assertTrue(foundChatHistory.isPresent());
        assertEquals("Updated message", foundChatHistory.get().getMessage());
        assertEquals("Updated response", foundChatHistory.get().getResponse());
    }
}
