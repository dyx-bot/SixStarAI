package com.example.appbacked.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LikeControllerTest {

    @Test
    void testLikeControllerLoads() {
        // 测试 LikeController 能正确加载
        LikeController controller = new LikeController();
        assertNotNull(controller);
    }

    @Test
    void testLikeControllerHasCorrectClassAnnotations() {
        // 验证类上有正确的注解
        assertTrue(LikeController.class.isAnnotationPresent(org.springframework.web.bind.annotation.RestController.class));
        assertTrue(LikeController.class.isAnnotationPresent(org.springframework.web.bind.annotation.RequestMapping.class));

        // 验证 RequestMapping 值
        org.springframework.web.bind.annotation.RequestMapping requestMapping =
            LikeController.class.getAnnotation(org.springframework.web.bind.annotation.RequestMapping.class);
        assertArrayEquals(new String[]{"/api/likes"}, requestMapping.value());
    }

    @Test
    void testLikeControllerHasAutowiredFields() throws NoSuchFieldException {
        // 验证控制器有正确的 Autowired 字段
        Field likeRepositoryField = LikeController.class.getDeclaredField("likeRepository");
        Field postRepositoryField = LikeController.class.getDeclaredField("postRepository");

        assertNotNull(likeRepositoryField);
        assertNotNull(postRepositoryField);

        assertTrue(likeRepositoryField.isAnnotationPresent(org.springframework.beans.factory.annotation.Autowired.class));
        assertTrue(postRepositoryField.isAnnotationPresent(org.springframework.beans.factory.annotation.Autowired.class));
    }

    @Test
    void testToggleLikeMethodExists() throws NoSuchMethodException {
        // 验证 toggleLike 方法存在且有正确的注解
        Method method = LikeController.class.getDeclaredMethod("toggleLike", ToggleLikeRequest.class);

        assertNotNull(method);
        assertTrue(method.isAnnotationPresent(org.springframework.web.bind.annotation.PostMapping.class));

        org.springframework.web.bind.annotation.PostMapping annotation =
            method.getAnnotation(org.springframework.web.bind.annotation.PostMapping.class);
        assertArrayEquals(new String[]{}, annotation.value()); // Empty means it uses class-level mapping
    }

    @Test
    void testUpdatePostLikeCountMethodExists() throws NoSuchMethodException {
        // 验证 updatePostLikeCount 私有方法存在
        Method method = LikeController.class.getDeclaredMethod("updatePostLikeCount", Long.class);
        assertNotNull(method);
    }

    @Test
    void testToggleLikeMethodReturnsCorrectType() throws NoSuchMethodException {
        // 验证 toggleLike 方法返回类型正确
        Method method = LikeController.class.getDeclaredMethod("toggleLike", ToggleLikeRequest.class);
        assertEquals(org.springframework.http.ResponseEntity.class, method.getReturnType());
    }

    @Test
    void testToggleLikeRequestClassExists() {
        // 测试 ToggleLikeRequest 类存在
        ToggleLikeRequest request = new ToggleLikeRequest();
        assertNotNull(request);
    }

    @Test
    void testToggleLikeRequestHasUserIdField() throws NoSuchFieldException {
        // 验证 ToggleLikeRequest 有 userId 字段及对应的 getter/setter
        Field userIdField = ToggleLikeRequest.class.getDeclaredField("userId");
        assertNotNull(userIdField);

        // 验证 getter 方法存在
        try {
            Method userIdGetter = ToggleLikeRequest.class.getDeclaredMethod("getUserId");
            assertNotNull(userIdGetter);
        } catch (NoSuchMethodException e) {
            fail("getUserId method should exist");
        }

        // 验证 setter 方法存在
        try {
            Method userIdSetter = ToggleLikeRequest.class.getDeclaredMethod("setUserId", Long.class);
            assertNotNull(userIdSetter);
        } catch (NoSuchMethodException e) {
            fail("setUserId method should exist");
        }
    }

    @Test
    void testToggleLikeRequestHasPostIdField() throws NoSuchFieldException {
        // 验证 ToggleLikeRequest 有 postId 字段及对应的 getter/setter
        Field postIdField = ToggleLikeRequest.class.getDeclaredField("postId");
        assertNotNull(postIdField);

        // 验证 getter 方法存在
        try {
            Method postIdGetter = ToggleLikeRequest.class.getDeclaredMethod("getPostId");
            assertNotNull(postIdGetter);
        } catch (NoSuchMethodException e) {
            fail("getPostId method should exist");
        }

        // 验证 setter 方法存在
        try {
            Method postIdSetter = ToggleLikeRequest.class.getDeclaredMethod("setPostId", Long.class);
            assertNotNull(postIdSetter);
        } catch (NoSuchMethodException e) {
            fail("setPostId method should exist");
        }
    }

    @Test
    void testToggleLikeRequestSetAndGetUserId() {
        // 测试 userId 的 getter 和 setter 方法
        ToggleLikeRequest request = new ToggleLikeRequest();
        Long userId = 123L;

        request.setUserId(userId);
        assertEquals(userId, request.getUserId());
    }

    @Test
    void testToggleLikeRequestSetAndGetPostId() {
        // 测试 postId 的 getter 和 setter 方法
        ToggleLikeRequest request = new ToggleLikeRequest();
        Long postId = 456L;

        request.setPostId(postId);
        assertEquals(postId, request.getPostId());
    }

    @Test
    void testToggleLikeRequestDefaultValues() {
        // 测试 ToggleLikeRequest 的默认值
        ToggleLikeRequest request = new ToggleLikeRequest();

        assertNull(request.getUserId());
        assertNull(request.getPostId());
    }

    @Test
    void testPostMappingAnnotationOnToggleLikeMethod() throws NoSuchMethodException {
        // 验证 toggleLike 方法上有正确的 PostMapping 注解
        Method toggleLikeMethod = LikeController.class.getDeclaredMethod(
            "toggleLike", ToggleLikeRequest.class);

        assertTrue(toggleLikeMethod.isAnnotationPresent(org.springframework.web.bind.annotation.PostMapping.class));

        // 验证 PostMapping 没有指定特定路径（默认为类级别路径）
        org.springframework.web.bind.annotation.PostMapping postMapping =
            toggleLikeMethod.getAnnotation(org.springframework.web.bind.annotation.PostMapping.class);
        assertArrayEquals(new String[]{}, postMapping.value()); // 空数组表示使用类级别的路径
    }

    @Test
    void testRequestBodyAnnotationOnToggleLikeMethod() throws NoSuchMethodException {
        // 验证 toggleLike 方法参数上有 RequestBody 注解
        Method toggleLikeMethod = LikeController.class.getDeclaredMethod(
            "toggleLike", ToggleLikeRequest.class);

        // 获取方法参数上的注解
        java.lang.reflect.Parameter[] parameters = toggleLikeMethod.getParameters();
        assertEquals(1, parameters.length);

        assertTrue(parameters[0].isAnnotationPresent(org.springframework.web.bind.annotation.RequestBody.class));
    }

    @Test
    void testLikeControllerNoArgsConstructor() {
        // 测试 LikeController 有默认构造函数
        try {
            LikeController controller = LikeController.class.newInstance();
            assertNotNull(controller);
        } catch (InstantiationException | IllegalAccessException e) {
            fail("LikeController should have a no-args constructor");
        }
    }

    @Test
    void testToggleLikeRequestMethodNoArgsConstructor() {
        // 测试 ToggleLikeRequest 有默认构造函数
        try {
            ToggleLikeRequest request = ToggleLikeRequest.class.newInstance();
            assertNotNull(request);
        } catch (InstantiationException | IllegalAccessException e) {
            fail("ToggleLikeRequest should have a no-args constructor");
        }
    }

    @Test
    void testToggleLikeRequestMethodAllArgsConstructor() {
        // 测试 ToggleLikeRequest 可以通过 setter 构造完整对象
        ToggleLikeRequest request = new ToggleLikeRequest();
        request.setUserId(1L);
        request.setPostId(2L);

        assertEquals(1L, request.getUserId());
        assertEquals(2L, request.getPostId());
    }

    @Test
    void testMethodParameterAnnotations() throws NoSuchMethodException {
        // 验证方法参数上的注解
        Method toggleLikeMethod = LikeController.class.getDeclaredMethod("toggleLike", ToggleLikeRequest.class);

        // 验证 @RequestBody 注解
        assertTrue(toggleLikeMethod.getParameters()[0].isAnnotationPresent(
            org.springframework.web.bind.annotation.RequestBody.class));
    }

    @Test
    void testUpdatePostLikeCountIsPrivate() throws NoSuchMethodException {
        // 验证 updatePostLikeCount 是私有方法
        Method method = LikeController.class.getDeclaredMethod("updatePostLikeCount", Long.class);
        assertTrue(java.lang.reflect.Modifier.isPrivate(method.getModifiers()));
    }

    @Test
    void testNoArgsConstructor() {
        // 测试 LikeController 有默认构造函数
        try {
            LikeController controller = LikeController.class.newInstance();
            assertNotNull(controller);
        } catch (InstantiationException | IllegalAccessException e) {
            fail("LikeController should have a no-args constructor");
        }
    }
}

