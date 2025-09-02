package com.example.appbacked.controller;

import com.example.appbacked.controller.FavoriteController;
import com.example.appbacked.controller.ToggleFavoriteRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FavoriteControllerTest {

    @Test
    void testFavoriteControllerLoads() {
        // 测试 FavoriteController 能正确加载
        FavoriteController controller = new FavoriteController();
        assertNotNull(controller);
    }

    @Test
    void testFavoriteControllerHasCorrectAnnotations() {
        // 验证类上有正确的注解
        assertTrue(FavoriteController.class.isAnnotationPresent(org.springframework.web.bind.annotation.RestController.class));
        assertTrue(FavoriteController.class.isAnnotationPresent(org.springframework.web.bind.annotation.RequestMapping.class));

        // 验证 RequestMapping 值
        org.springframework.web.bind.annotation.RequestMapping requestMapping =
            FavoriteController.class.getAnnotation(org.springframework.web.bind.annotation.RequestMapping.class);
        assertArrayEquals(new String[]{"/api/favorites"}, requestMapping.value());
    }

    @Test
    void testFavoriteControllerHasAutowiredField() throws NoSuchFieldException {
        // 验证控制器有正确的 Autowired 字段
        Field favoriteRepositoryField = FavoriteController.class.getDeclaredField("favoriteRepository");
        assertNotNull(favoriteRepositoryField);
        assertTrue(favoriteRepositoryField.isAnnotationPresent(org.springframework.beans.factory.annotation.Autowired.class));
    }

    @Test
    void testToggleFavoriteMethodExists() throws NoSuchMethodException {
        // 验证 toggleFavorite 方法存在且有正确的注解
        Method toggleFavoriteMethod = FavoriteController.class.getDeclaredMethod(
            "toggleFavorite", ToggleFavoriteRequest.class);

        assertNotNull(toggleFavoriteMethod);
        assertTrue(toggleFavoriteMethod.isAnnotationPresent(org.springframework.web.bind.annotation.PostMapping.class));
    }

    @Test
    void testToggleFavoriteMethodReturnsCorrectType() throws NoSuchMethodException {
        // 验证 toggleFavorite 方法返回类型正确
        Method toggleFavoriteMethod = FavoriteController.class.getDeclaredMethod(
            "toggleFavorite", ToggleFavoriteRequest.class);

        assertEquals(org.springframework.http.ResponseEntity.class, toggleFavoriteMethod.getReturnType());
    }

    @Test
    void testToggleFavoriteRequestClassExists() {
        // 测试 ToggleFavoriteRequest 类存在
        ToggleFavoriteRequest request = new ToggleFavoriteRequest();
        assertNotNull(request);
    }

    @Test
    void testToggleFavoriteRequestHasUserIdField() throws NoSuchFieldException {
        // 验证 ToggleFavoriteRequest 有 userId 字段及对应的 getter/setter
        Field userIdField = ToggleFavoriteRequest.class.getDeclaredField("userId");
        assertNotNull(userIdField);

        // 验证 getter 方法存在
        try {
            Method userIdGetter = ToggleFavoriteRequest.class.getDeclaredMethod("getUserId");
            assertNotNull(userIdGetter);
        } catch (NoSuchMethodException e) {
            fail("getUserId method should exist");
        }

        // 验证 setter 方法存在
        try {
            Method userIdSetter = ToggleFavoriteRequest.class.getDeclaredMethod("setUserId", Long.class);
            assertNotNull(userIdSetter);
        } catch (NoSuchMethodException e) {
            fail("setUserId method should exist");
        }
    }

    @Test
    void testToggleFavoriteRequestHasPostIdField() throws NoSuchFieldException {
        // 验证 ToggleFavoriteRequest 有 postId 字段及对应的 getter/setter
        Field postIdField = ToggleFavoriteRequest.class.getDeclaredField("postId");
        assertNotNull(postIdField);

        // 验证 getter 方法存在
        try {
            Method postIdGetter = ToggleFavoriteRequest.class.getDeclaredMethod("getPostId");
            assertNotNull(postIdGetter);
        } catch (NoSuchMethodException e) {
            fail("getPostId method should exist");
        }

        // 验证 setter 方法存在
        try {
            Method postIdSetter = ToggleFavoriteRequest.class.getDeclaredMethod("setPostId", Long.class);
            assertNotNull(postIdSetter);
        } catch (NoSuchMethodException e) {
            fail("setPostId method should exist");
        }
    }

    @Test
    void testToggleFavoriteRequestSetAndGetUserId() {
        // 测试 userId 的 getter 和 setter 方法
        ToggleFavoriteRequest request = new ToggleFavoriteRequest();
        Long userId = 123L;

        request.setUserId(userId);
        assertEquals(userId, request.getUserId());
    }

    @Test
    void testToggleFavoriteRequestSetAndGetPostId() {
        // 测试 postId 的 getter 和 setter 方法
        ToggleFavoriteRequest request = new ToggleFavoriteRequest();
        Long postId = 456L;

        request.setPostId(postId);
        assertEquals(postId, request.getPostId());
    }

    @Test
    void testToggleFavoriteRequestDefaultValues() {
        // 测试 ToggleFavoriteRequest 的默认值
        ToggleFavoriteRequest request = new ToggleFavoriteRequest();

        assertNull(request.getUserId());
        assertNull(request.getPostId());
    }

    @Test
    void testPostMappingAnnotationOnToggleFavoriteMethod() throws NoSuchMethodException {
        // 验证 toggleFavorite 方法上有正确的 PostMapping 注解
        Method toggleFavoriteMethod = FavoriteController.class.getDeclaredMethod(
            "toggleFavorite", ToggleFavoriteRequest.class);

        assertTrue(toggleFavoriteMethod.isAnnotationPresent(org.springframework.web.bind.annotation.PostMapping.class));

        // 验证 PostMapping 没有指定特定路径（默认为类级别路径）
        org.springframework.web.bind.annotation.PostMapping postMapping =
            toggleFavoriteMethod.getAnnotation(org.springframework.web.bind.annotation.PostMapping.class);
        assertArrayEquals(new String[]{}, postMapping.value()); // 空数组表示使用类级别的路径
    }

    @Test
    void testRequestBodyAnnotationOnToggleFavoriteMethod() throws NoSuchMethodException {
        // 验证 toggleFavorite 方法参数上有 RequestBody 注解
        Method toggleFavoriteMethod = FavoriteController.class.getDeclaredMethod(
            "toggleFavorite", ToggleFavoriteRequest.class);

        // 获取方法参数上的注解
        java.lang.reflect.Parameter[] parameters = toggleFavoriteMethod.getParameters();
        assertEquals(1, parameters.length);

        assertTrue(parameters[0].isAnnotationPresent(org.springframework.web.bind.annotation.RequestBody.class));
    }

    @Test
    void testFavoriteControllerNoArgsConstructor() {
        // 测试 FavoriteController 有默认构造函数
        try {
            FavoriteController controller = FavoriteController.class.newInstance();
            assertNotNull(controller);
        } catch (InstantiationException | IllegalAccessException e) {
            fail("FavoriteController should have a no-args constructor");
        }
    }

    @Test
    void testToggleFavoriteRequestMethodNoArgsConstructor() {
        // 测试 ToggleFavoriteRequest 有默认构造函数
        try {
            ToggleFavoriteRequest request = ToggleFavoriteRequest.class.newInstance();
            assertNotNull(request);
        } catch (InstantiationException | IllegalAccessException e) {
            fail("ToggleFavoriteRequest should have a no-args constructor");
        }
    }

    @Test
    void testToggleFavoriteRequestMethodAllArgsConstructor() {
        // 测试 ToggleFavoriteRequest 可以通过 setter 构造完整对象
        ToggleFavoriteRequest request = new ToggleFavoriteRequest();
        request.setUserId(1L);
        request.setPostId(2L);

        assertEquals(1L, request.getUserId());
        assertEquals(2L, request.getPostId());
    }
}
