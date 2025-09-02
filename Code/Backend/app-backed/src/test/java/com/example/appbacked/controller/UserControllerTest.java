package com.example.appbacked.controller;

import com.example.appbacked.dto.PostDetailDTO;
import com.example.appbacked.dto.UserDetailDTO;
import com.example.appbacked.entity.User;
import com.example.appbacked.repository.FavoriteRepository;
import com.example.appbacked.repository.LikeRepository;
import com.example.appbacked.repository.PostRepository;
import com.example.appbacked.repository.UserRepository;
import com.example.appbacked.service.PostService;
import com.example.appbacked.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        // 使用反射设置私有字段
        setPrivateField(userController, "uploadDir", "uploads/avatars/");
        setPrivateField(userController, "serverBaseUrl", "http://localhost:8080");
    }

    // 反射工具方法，用于设置私有字段
    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testGetUserDetail_Success() {
        // Given
        Long userId = 1L;
        Long currentUserId = 2L;

        User user = new User();
        user.setId(userId);
        user.setUsername("testUser");
        user.setAvatar("avatar.png");
        user.setGrade("freshman");
        user.setCollege("Computer Science");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(postRepository.countByUserId(userId)).thenReturn(5L);
        when(likeRepository.countByUserId(userId)).thenReturn(10L);
        when(likeRepository.countByPostUserId(userId)).thenReturn(3L);
        when(favoriteRepository.countByPostUserId(userId)).thenReturn(2L);
        when(favoriteRepository.countByUserId(userId)).thenReturn(4L);

        List<PostDetailDTO> userPosts = new ArrayList<>();
        List<PostDetailDTO> likedPosts = new ArrayList<>();
        List<PostDetailDTO> favoritePosts = new ArrayList<>();

        when(postService.getPostsByUserId(userId)).thenReturn(userPosts);
        when(postService.getLikedPostsByUserId(userId)).thenReturn(likedPosts);
        when(postService.getFavoritePostsByUserId(userId)).thenReturn(favoritePosts);

        // When
        ResponseEntity<?> response = userController.getUserDetail(userId, currentUserId);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof UserDetailDTO);

        UserDetailDTO userDetail = (UserDetailDTO) response.getBody();
        assertEquals(userId, userDetail.getId());
        assertEquals("testUser", userDetail.getUsername());
        assertEquals(5L, userDetail.getPostCount());
        assertEquals(10L, userDetail.getMyLikedPostCount());
        assertEquals(3L, userDetail.getReceivedLikeCount());
        assertEquals(2L, userDetail.getReceivedFavoriteCount());
        assertEquals(4L, userDetail.getFavoritePostCount());

        verify(userRepository, times(1)).findById(userId);
        verify(postRepository, times(1)).countByUserId(userId);
        verify(likeRepository, times(1)).countByUserId(userId);
        verify(likeRepository, times(1)).countByPostUserId(userId);
        verify(favoriteRepository, times(1)).countByPostUserId(userId);
        verify(favoriteRepository, times(1)).countByUserId(userId);
        verify(postService, times(1)).getPostsByUserId(userId);
        verify(postService, times(1)).getLikedPostsByUserId(userId);
        verify(postService, times(1)).getFavoritePostsByUserId(userId);
    }

    @Test
    void testGetUserDetail_UserNotFound() {
        // Given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        ResponseEntity<?> response = userController.getUserDetail(userId, null);

        // Then
        assertEquals(404, response.getStatusCodeValue());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testUpdateUser_SuccessWithUsernameAndAvatar() throws IOException {
        // Given
        Long userId = 1L;
        User user = new User();
        user.setUsername("newUsername");
        user.setAvatar("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8/5+hHgAHggJ/PchI7wAAAABJRU5ErkJggg==");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setUsername("newUsername");
        updatedUser.setAvatar("http://localhost:8080/uploads/avatars/avatar_1_1234567890.png");

        when(userService.updateUser(eq(userId), anyString(), anyString())).thenReturn(updatedUser);

        // When
        ResponseEntity<?> response = userController.updateUser(userId, user);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertTrue((Boolean) responseBody.get("success"));
        assertEquals("用户信息更新成功", responseBody.get("message"));
        assertNotNull(responseBody.get("data"));

        verify(userService, times(1)).updateUser(eq(userId), anyString(), anyString());
    }

    @Test
    void testUpdateUser_IllegalArgumentException() throws IOException {
        // Given
        Long userId = 1L;
        User user = new User();
        user.setUsername("newUsername");
        user.setAvatar("invalidBase64");

        doThrow(new IllegalArgumentException("Invalid user"))
                .when(userService).updateUser(eq(userId), anyString(), anyString());

        // When
        ResponseEntity<?> response = userController.updateUser(userId, user);

        // Then
        assertEquals(400, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertFalse((Boolean) responseBody.get("success"));
        assertNotNull(responseBody.get("message"));

        verify(userService, times(1)).updateUser(eq(userId), anyString(), anyString());
    }

    @Test
    void testUpdateUsername_Success() {
        // Given
        Long userId = 1L;
        Map<String, String> request = new HashMap<>();
        request.put("username", "newUsername");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setUsername("newUsername");

        when(userService.updateUser(eq(userId), anyString(), any())).thenReturn(updatedUser);

        // When
        ResponseEntity<?> response = userController.updateUsername(userId, request);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertTrue((Boolean) responseBody.get("success"));
        assertEquals("用户名更新成功", responseBody.get("message"));
        assertEquals("newUsername", responseBody.get("username"));

        verify(userService, times(1)).updateUser(eq(userId), eq("newUsername"), any());
    }

    @Test
    void testUpdateUsername_EmptyUsername() {
        // Given
        Long userId = 1L;
        Map<String, String> request = new HashMap<>();
        request.put("username", "");

        // When
        ResponseEntity<?> response = userController.updateUsername(userId, request);

        // Then
        assertEquals(400, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertFalse((Boolean) responseBody.get("success"));
        assertNotNull(responseBody.get("message"));
    }

    @Test
    void testUpdateAvatar_Success() throws IOException {
        // Given
        Long userId = 1L;
        Map<String, String> request = new HashMap<>();
        request.put("avatar", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8/5+hHgAHggJ/PchI7wAAAABJRU5ErkJggg==");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setAvatar("http://localhost:8080/uploads/avatars/avatar_1_1234567890.png");

        when(userService.updateUser(eq(userId), any(), anyString())).thenReturn(updatedUser);

        // When
        ResponseEntity<?> response = userController.updateAvatar(userId, request);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertTrue((Boolean) responseBody.get("success"));
        assertNotNull(responseBody.get("avatar"));

        verify(userService, times(1)).updateUser(eq(userId), any(), anyString());
    }

    @Test
    void testUpdateAvatar_EmptyAvatarData() {
        // Given
        Long userId = 1L;
        Map<String, String> request = new HashMap<>();
        request.put("avatar", "");

        // When
        ResponseEntity<?> response = userController.updateAvatar(userId, request);

        // Then
        assertEquals(400, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertFalse((Boolean) responseBody.get("success"));
        assertNotNull(responseBody.get("message"));
    }

    @Test
    void testGetMyPosts_Success() {
        // Given
        Long userId = 1L;
        List<PostDetailDTO> posts = new ArrayList<>();
        when(postService.getPostsByUserId(userId)).thenReturn(posts);

        // When
        ResponseEntity<List<?>> response = userController.getMyPosts(userId);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(posts, response.getBody());
        verify(postService, times(1)).getPostsByUserId(userId);
    }

    @Test
    void testGetMyPosts_Exception() {
        // Given
        Long userId = 1L;
        when(postService.getPostsByUserId(userId)).thenThrow(new RuntimeException("Database error"));

        // When
        ResponseEntity<List<?>> response = userController.getMyPosts(userId);

        // Then
        assertEquals(500, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(postService, times(1)).getPostsByUserId(userId);
    }

    @Test
    void testGetFavoritePosts_Success() {
        // Given
        Long userId = 1L;
        List<PostDetailDTO> posts = new ArrayList<>();
        when(postService.getFavoritePostsByUserId(userId)).thenReturn(posts);

        // When
        ResponseEntity<List<?>> response = userController.getFavoritePosts(userId);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(posts, response.getBody());
        verify(postService, times(1)).getFavoritePostsByUserId(userId);
    }

    @Test
    void testGetFavoritePosts_Exception() {
        // Given
        Long userId = 1L;
        when(postService.getFavoritePostsByUserId(userId)).thenThrow(new RuntimeException("Database error"));

        // When
        ResponseEntity<List<?>> response = userController.getFavoritePosts(userId);

        // Then
        assertEquals(500, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(postService, times(1)).getFavoritePostsByUserId(userId);
    }

    @Test
    void testGetLikedPosts_Success() {
        // Given
        Long userId = 1L;
        List<PostDetailDTO> posts = new ArrayList<>();
        when(postService.getLikedPostsByUserId(userId)).thenReturn(posts);

        // When
        ResponseEntity<List<?>> response = userController.getLikedPosts(userId);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(posts, response.getBody());
        verify(postService, times(1)).getLikedPostsByUserId(userId);
    }

    @Test
    void testGetLikedPosts_Exception() {
        // Given
        Long userId = 1L;
        when(postService.getLikedPostsByUserId(userId)).thenThrow(new RuntimeException("Database error"));

        // When
        ResponseEntity<List<?>> response = userController.getLikedPosts(userId);

        // Then
        assertEquals(500, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(postService, times(1)).getLikedPostsByUserId(userId);
    }
}
