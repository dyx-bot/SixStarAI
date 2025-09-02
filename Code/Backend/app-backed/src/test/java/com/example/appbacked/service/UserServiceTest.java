package com.example.appbacked.service;

import com.example.appbacked.entity.User;
import com.example.appbacked.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    // 注入被测试的UserService
    @InjectMocks
    private UserService userService;

    // 模拟依赖的UserRepository
    @Mock
    private UserRepository userRepository;

    // 模拟Logger
    @Mock
    private Logger logger;

    @BeforeEach
    void setUp() {
        // 初始化Mockito注解
        MockitoAnnotations.openMocks(this);

        // 设置uploadDir属性
        ReflectionTestUtils.setField(userService, "uploadDir", "D:/test/uploads");

        // 重新初始化logger，避免静态final字段问题
        try {
            ReflectionTestUtils.setField(userService, "logger", logger);
        } catch (Exception e) {
            // 如果仍然失败，可以使用其他方式处理
            System.err.println("Warning: Cannot set logger field, continuing without it.");
        }
    }

    @Test
    void testLoginSuccess() {
        // 准备测试数据
        User inputUser = new User();
        inputUser.setId(1001L);
        inputUser.setPassword("123456");

        User dbUser = new User();
        dbUser.setId(1001L);
        dbUser.setPassword("8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92"); // SHA-256 of "123456"
        dbUser.setUsername("测试用户");
        dbUser.setGrade("大一");
        dbUser.setCollege("计算机学院");

        // 设置模拟行为
        when(userRepository.findById(1001L)).thenReturn(Optional.of(dbUser));

        // 执行测试
        boolean result = userService.login(inputUser);

        // 验证结果
        assertTrue(result);
        assertEquals("测试用户", inputUser.getUsername());
        assertEquals("大一", inputUser.getGrade());
        assertEquals("计算机学院", inputUser.getCollege());

        // 验证方法调用
        verify(userRepository, times(1)).findById(1001L);
    }

    @Test
    void testLoginFailureWithWrongPassword() {
        // 准备测试数据
        User inputUser = new User();
        inputUser.setId(1001L);
        inputUser.setPassword("wrongpassword");

        User dbUser = new User();
        dbUser.setId(1001L);
        dbUser.setPassword("8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92"); // SHA-256 of "123456"

        // 设置模拟行为
        when(userRepository.findById(1001L)).thenReturn(Optional.of(dbUser));

        // 执行测试
        boolean result = userService.login(inputUser);

        // 验证结果
        assertFalse(result);

        // 验证方法调用
        verify(userRepository, times(1)).findById(1001L);
    }

    @Test
    void testLoginFailureWithNonExistentUser() {
        // 准备测试数据
        User inputUser = new User();
        inputUser.setId(9999L);
        inputUser.setPassword("123456");

        // 设置模拟行为
        when(userRepository.findById(9999L)).thenReturn(Optional.empty());

        // 执行测试
        boolean result = userService.login(inputUser);

        // 验证结果
        assertFalse(result);

        // 验证方法调用
        verify(userRepository, times(1)).findById(9999L);
    }

    @Test
    void testLoginWithNullUser() {
        // 执行测试
        boolean result = userService.login(null);

        // 验证结果
        assertFalse(result);
    }

    @Test
    void testUpdateUserSuccess() {
        // 准备测试数据
        Long userId = 1001L;
        String newUsername = "新用户名";
        String newAvatar = "https://example.com/avatar.jpg";

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("旧用户名");
        existingUser.setAvatar("https://old-example.com/avatar.jpg");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setUsername(newUsername);
        updatedUser.setAvatar(newAvatar);

        // 设置模拟行为
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // 执行测试
        User result = userService.updateUser(userId, newUsername, newAvatar);

        // 验证结果
        assertNotNull(result);
        assertEquals(newUsername, result.getUsername());
        assertEquals(newAvatar, result.getAvatar());

        // 验证方法调用
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testDeleteUser() {
        // 准备测试数据
        Long userId = 1001L;

        // 执行测试
        userService.deleteUser(userId);

        // 验证方法调用
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testUpdateUserRole() {
        // 准备测试数据
        Long userId = 1001L;
        String newRole = "ADMIN";

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setRole("USER");

        // 设置模拟行为
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        // 执行测试
        userService.updateUserRole(userId, newRole);

        // 验证结果
        assertEquals(newRole, existingUser.getRole());

        // 验证方法调用
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }
}
