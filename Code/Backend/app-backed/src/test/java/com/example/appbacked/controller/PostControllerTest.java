package com.example.appbacked.controller;

import com.example.appbacked.dto.CreatePostRequest;
import com.example.appbacked.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostControllerTest {

    @Test
    void testPostControllerLoads() {
        // 测试 PostController 能正确加载
        PostController controller = new PostController();
        assertNotNull(controller);
    }

    @Test
    void testPostControllerHasCorrectClassAnnotations() {
        // 验证类上有正确的注解
        assertTrue(PostController.class.isAnnotationPresent(org.springframework.web.bind.annotation.RestController.class));
        assertTrue(PostController.class.isAnnotationPresent(org.springframework.web.bind.annotation.RequestMapping.class));

        // 验证 RequestMapping 值
        org.springframework.web.bind.annotation.RequestMapping requestMapping =
            PostController.class.getAnnotation(org.springframework.web.bind.annotation.RequestMapping.class);
        assertArrayEquals(new String[]{"/api/posts"}, requestMapping.value());
    }

    @Test
    void testPostControllerHasAutowiredFields() throws NoSuchFieldException {
        // 验证控制器有正确的 Autowired 字段
        Field postServiceField = PostController.class.getDeclaredField("postService");
        Field postRepositoryField = PostController.class.getDeclaredField("postRepository");
        Field userRepositoryField = PostController.class.getDeclaredField("userRepository");

        assertNotNull(postServiceField);
        assertNotNull(postRepositoryField);
        assertNotNull(userRepositoryField);

        assertTrue(postServiceField.isAnnotationPresent(org.springframework.beans.factory.annotation.Autowired.class));
        assertTrue(postRepositoryField.isAnnotationPresent(org.springframework.beans.factory.annotation.Autowired.class));
        assertTrue(userRepositoryField.isAnnotationPresent(org.springframework.beans.factory.annotation.Autowired.class));
    }

    @Test
    void testCreatePostMethodExists() throws NoSuchMethodException {
        // 验证 createPost 方法存在且有正确的注解
        Method method = PostController.class.getDeclaredMethod("createPost", CreatePostRequest.class);
        assertNotNull(method);
        assertTrue(method.isAnnotationPresent(org.springframework.web.bind.annotation.PostMapping.class));
        // 注意：@RestController 已经包含了 @ResponseBody 的功能，所以不需要单独的 @ResponseBody 注解

        org.springframework.web.bind.annotation.PostMapping annotation =
            method.getAnnotation(org.springframework.web.bind.annotation.PostMapping.class);
        assertArrayEquals(new String[]{}, annotation.value()); // Empty means it uses class-level mapping
    }

    @Test
    void testGetAllPostsMethodExists() throws NoSuchMethodException {
        // 验证 getAllPosts 方法存在且有正确的注解
        Method method = PostController.class.getDeclaredMethod("getAllPosts", Long.class);
        assertNotNull(method);
        assertTrue(method.isAnnotationPresent(org.springframework.web.bind.annotation.GetMapping.class));

        org.springframework.web.bind.annotation.GetMapping annotation =
            method.getAnnotation(org.springframework.web.bind.annotation.GetMapping.class);
        assertArrayEquals(new String[]{"/get"}, annotation.value());
    }

    @Test
    void testGetMethodExists() throws NoSuchMethodException {
        // 验证 getPost 方法存在且有正确的注解
        Method method = PostController.class.getDeclaredMethod("getPost", Long.class, Long.class);
        assertNotNull(method);
        assertTrue(method.isAnnotationPresent(org.springframework.web.bind.annotation.GetMapping.class));

        org.springframework.web.bind.annotation.GetMapping annotation =
            method.getAnnotation(org.springframework.web.bind.annotation.GetMapping.class);
        assertArrayEquals(new String[]{"/{id}"}, annotation.value());
    }

    @Test
    void testGetPostsByUserIdMethodExists() throws NoSuchMethodException {
        // 验证 getPostsByUserId 方法存在且有正确的注解
        Method method = PostController.class.getDeclaredMethod("getPostsByUserId", Long.class, Long.class);
        assertNotNull(method);
        assertTrue(method.isAnnotationPresent(org.springframework.web.bind.annotation.GetMapping.class));

        org.springframework.web.bind.annotation.GetMapping annotation =
            method.getAnnotation(org.springframework.web.bind.annotation.GetMapping.class);
        assertArrayEquals(new String[]{"/user/{userId}"}, annotation.value());
    }

    @Test
    void testGetLikedPostsByUserIdMethodExists() throws NoSuchMethodException {
        // 验证 getLikedPostsByUserId 方法存在且有正确的注解
        Method method = PostController.class.getDeclaredMethod("getLikedPostsByUserId", Long.class, Long.class);
        assertNotNull(method);
        assertTrue(method.isAnnotationPresent(org.springframework.web.bind.annotation.GetMapping.class));

        org.springframework.web.bind.annotation.GetMapping annotation =
            method.getAnnotation(org.springframework.web.bind.annotation.GetMapping.class);
        assertArrayEquals(new String[]{"/liked/{userId}"}, annotation.value());
    }

    @Test
    void testGetFavoritePostsByUserIdMethodExists() throws NoSuchMethodException {
        // 验证 getFavoritePostsByUserId 方法存在且有正确的注解
        Method method = PostController.class.getDeclaredMethod("getFavoritePostsByUserId", Long.class, Long.class);
        assertNotNull(method);
        assertTrue(method.isAnnotationPresent(org.springframework.web.bind.annotation.GetMapping.class));

        org.springframework.web.bind.annotation.GetMapping annotation =
            method.getAnnotation(org.springframework.web.bind.annotation.GetMapping.class);
        assertArrayEquals(new String[]{"/favorite/{userId}"}, annotation.value());
    }

    @Test
    void testDeletePostMethodExists() throws NoSuchMethodException {
        // 验证 deletePost 方法存在且有正确的注解
        Method method = PostController.class.getDeclaredMethod("deletePost", Long.class, Long.class);
        assertNotNull(method);
        assertTrue(method.isAnnotationPresent(org.springframework.web.bind.annotation.DeleteMapping.class));

        org.springframework.web.bind.annotation.DeleteMapping annotation =
            method.getAnnotation(org.springframework.web.bind.annotation.DeleteMapping.class);
        assertArrayEquals(new String[]{"/{postId}"}, annotation.value());
    }

    @Test
    void testMethodReturnTypes() throws NoSuchMethodException {
        // 验证所有方法返回类型正确
        Method createPostMethod = PostController.class.getDeclaredMethod("createPost", CreatePostRequest.class);
        Method getAllPostsMethod = PostController.class.getDeclaredMethod("getAllPosts", Long.class);
        Method getPostMethod = PostController.class.getDeclaredMethod("getPost", Long.class, Long.class);
        Method getPostsByUserIdMethod = PostController.class.getDeclaredMethod("getPostsByUserId", Long.class, Long.class);
        Method getLikedPostsByUserIdMethod = PostController.class.getDeclaredMethod("getLikedPostsByUserId", Long.class, Long.class);
        Method getFavoritePostsByUserIdMethod = PostController.class.getDeclaredMethod("getFavoritePostsByUserId", Long.class, Long.class);
        Method deletePostMethod = PostController.class.getDeclaredMethod("deletePost", Long.class, Long.class);

        assertEquals(org.springframework.http.ResponseEntity.class, createPostMethod.getReturnType());
        assertEquals(org.springframework.http.ResponseEntity.class, getAllPostsMethod.getReturnType());
        assertEquals(org.springframework.http.ResponseEntity.class, getPostMethod.getReturnType());
        assertEquals(org.springframework.http.ResponseEntity.class, getPostsByUserIdMethod.getReturnType());
        assertEquals(org.springframework.http.ResponseEntity.class, getLikedPostsByUserIdMethod.getReturnType());
        assertEquals(org.springframework.http.ResponseEntity.class, getFavoritePostsByUserIdMethod.getReturnType());
        assertEquals(org.springframework.http.ResponseEntity.class, deletePostMethod.getReturnType());
    }

    @Test
    void testMethodParameterAnnotations() throws NoSuchMethodException {
        // 验证方法参数上的注解

        // createPost 方法
        Method createPostMethod = PostController.class.getDeclaredMethod("createPost", CreatePostRequest.class);
        assertTrue(createPostMethod.getParameters()[0].isAnnotationPresent(
            org.springframework.web.bind.annotation.RequestBody.class));
        assertTrue(createPostMethod.getParameters()[0].isAnnotationPresent(
            jakarta.validation.Valid.class));

        // getAllPosts 方法
        Method getAllPostsMethod = PostController.class.getDeclaredMethod("getAllPosts", Long.class);
        assertTrue(getAllPostsMethod.getParameters()[0].isAnnotationPresent(
            org.springframework.web.bind.annotation.RequestParam.class));

        // getPost 方法
        Method getPostMethod = PostController.class.getDeclaredMethod("getPost", Long.class, Long.class);
        assertTrue(getPostMethod.getParameters()[0].isAnnotationPresent(
            org.springframework.web.bind.annotation.PathVariable.class));
        assertTrue(getPostMethod.getParameters()[1].isAnnotationPresent(
            org.springframework.web.bind.annotation.RequestParam.class));

        // getPostsByUserId 方法
        Method getPostsByUserIdMethod = PostController.class.getDeclaredMethod("getPostsByUserId", Long.class, Long.class);
        assertTrue(getPostsByUserIdMethod.getParameters()[0].isAnnotationPresent(
            org.springframework.web.bind.annotation.PathVariable.class));
        assertTrue(getPostsByUserIdMethod.getParameters()[1].isAnnotationPresent(
            org.springframework.web.bind.annotation.RequestParam.class));

        // getLikedPostsByUserId 方法
        Method getLikedPostsByUserIdMethod = PostController.class.getDeclaredMethod("getLikedPostsByUserId", Long.class, Long.class);
        assertTrue(getLikedPostsByUserIdMethod.getParameters()[0].isAnnotationPresent(
            org.springframework.web.bind.annotation.PathVariable.class));
        assertTrue(getLikedPostsByUserIdMethod.getParameters()[1].isAnnotationPresent(
            org.springframework.web.bind.annotation.RequestParam.class));

        // getFavoritePostsByUserId 方法
        Method getFavoritePostsByUserIdMethod = PostController.class.getDeclaredMethod("getFavoritePostsByUserId", Long.class, Long.class);
        assertTrue(getFavoritePostsByUserIdMethod.getParameters()[0].isAnnotationPresent(
            org.springframework.web.bind.annotation.PathVariable.class));
        assertTrue(getFavoritePostsByUserIdMethod.getParameters()[1].isAnnotationPresent(
            org.springframework.web.bind.annotation.RequestParam.class));

        // deletePost 方法
        Method deletePostMethod = PostController.class.getDeclaredMethod("deletePost", Long.class, Long.class);
        assertTrue(deletePostMethod.getParameters()[0].isAnnotationPresent(
            org.springframework.web.bind.annotation.PathVariable.class));
        assertTrue(deletePostMethod.getParameters()[1].isAnnotationPresent(
            org.springframework.web.bind.annotation.RequestParam.class));
    }

    @Test
    void testRequestParamDetails() throws NoSuchMethodException {
        // 验证 @RequestParam 的详细信息

        // getAllPosts 方法
        Method getAllPostsMethod = PostController.class.getDeclaredMethod("getAllPosts", Long.class);
        org.springframework.web.bind.annotation.RequestParam getAllPostsParam =
            getAllPostsMethod.getParameters()[0].getAnnotation(org.springframework.web.bind.annotation.RequestParam.class);
        assertFalse(getAllPostsParam.required()); // userId is not required

        // getPostsByUserId 方法
        Method getPostsByUserIdMethod = PostController.class.getDeclaredMethod("getPostsByUserId", Long.class, Long.class);
        org.springframework.web.bind.annotation.RequestParam getPostsByUserIdParam =
            getPostsByUserIdMethod.getParameters()[1].getAnnotation(org.springframework.web.bind.annotation.RequestParam.class);
        assertFalse(getPostsByUserIdParam.required()); // currentUserId is not required
        assertEquals("0", getPostsByUserIdParam.defaultValue()); // default value is "0"

        // getLikedPostsByUserId 方法
        Method getLikedPostsByUserIdMethod = PostController.class.getDeclaredMethod("getLikedPostsByUserId", Long.class, Long.class);
        org.springframework.web.bind.annotation.RequestParam getLikedPostsByUserIdParam =
            getLikedPostsByUserIdMethod.getParameters()[1].getAnnotation(org.springframework.web.bind.annotation.RequestParam.class);
        assertFalse(getLikedPostsByUserIdParam.required()); // currentUserId is not required
        assertEquals("0", getLikedPostsByUserIdParam.defaultValue()); // default value is "0"

        // getFavoritePostsByUserId 方法
        Method getFavoritePostsByUserIdMethod = PostController.class.getDeclaredMethod("getFavoritePostsByUserId", Long.class, Long.class);
        org.springframework.web.bind.annotation.RequestParam getFavoritePostsByUserIdParam =
            getFavoritePostsByUserIdMethod.getParameters()[1].getAnnotation(org.springframework.web.bind.annotation.RequestParam.class);
        assertFalse(getFavoritePostsByUserIdParam.required()); // currentUserId is not required
        assertEquals("0", getFavoritePostsByUserIdParam.defaultValue()); // default value is "0"
    }

    @Test
    void testNoArgsConstructor() {
        // 测试 PostController 有默认构造函数
        try {
            PostController controller = PostController.class.newInstance();
            assertNotNull(controller);
        } catch (InstantiationException | IllegalAccessException e) {
            fail("PostController should have a no-args constructor");
        }
    }

    @Test
    void testControllerClassStructure() {
        // 验证控制器类结构
        Class<?> clazz = PostController.class;

        // 检查类是 public 的
        assertTrue(java.lang.reflect.Modifier.isPublic(clazz.getModifiers()));

        // 检查类不是 abstract 的
        assertFalse(java.lang.reflect.Modifier.isAbstract(clazz.getModifiers()));

        // 检查类不是 interface
        assertFalse(clazz.isInterface());
    }

    @Test
    void testAutowiredFieldTypes() throws NoSuchFieldException {
        // 验证 Autowired 字段的类型
        Field postServiceField = PostController.class.getDeclaredField("postService");
        Field postRepositoryField = PostController.class.getDeclaredField("postRepository");
        Field userRepositoryField = PostController.class.getDeclaredField("userRepository");

        assertEquals(com.example.appbacked.service.PostService.class, postServiceField.getType());
        assertEquals(com.example.appbacked.repository.PostRepository.class, postRepositoryField.getType());
        assertEquals(com.example.appbacked.repository.UserRepository.class, userRepositoryField.getType()); // 修正为正确的类
    }

    @Test
    void testAutowiredFieldModifiers() throws NoSuchFieldException {
        // 验证 Autowired 字段的修饰符
        Field postServiceField = PostController.class.getDeclaredField("postService");
        Field postRepositoryField = PostController.class.getDeclaredField("postRepository");
        Field userRepositoryField = PostController.class.getDeclaredField("userRepository");

        // 检查字段是 private 的
        assertTrue(java.lang.reflect.Modifier.isPrivate(postServiceField.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isPrivate(postRepositoryField.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isPrivate(userRepositoryField.getModifiers()));
    }

    @Test
    void testPostMappingAnnotationDetails() throws NoSuchMethodException {
        // 验证 PostMapping 注解的详细信息
        Method createPostMethod = PostController.class.getDeclaredMethod("createPost", CreatePostRequest.class);
        org.springframework.web.bind.annotation.PostMapping postMapping =
            createPostMethod.getAnnotation(org.springframework.web.bind.annotation.PostMapping.class);

        // 检查 value 属性
        assertArrayEquals(new String[]{}, postMapping.value());
    }

    @Test
    void testGetMappingAnnotationDetails() throws NoSuchMethodException {
        // 验证 GetMapping 注解的详细信息

        // getAllPosts 方法
        Method getAllPostsMethod = PostController.class.getDeclaredMethod("getAllPosts", Long.class);
        org.springframework.web.bind.annotation.GetMapping getAllPostsMapping =
            getAllPostsMethod.getAnnotation(org.springframework.web.bind.annotation.GetMapping.class);
        assertArrayEquals(new String[]{"/get"}, getAllPostsMapping.value());

        // getPost 方法
        Method getPostMethod = PostController.class.getDeclaredMethod("getPost", Long.class, Long.class);
        org.springframework.web.bind.annotation.GetMapping getPostMapping =
            getPostMethod.getAnnotation(org.springframework.web.bind.annotation.GetMapping.class);
        assertArrayEquals(new String[]{"/{id}"}, getPostMapping.value());

        // getPostsByUserId 方法
        Method getPostsByUserIdMethod = PostController.class.getDeclaredMethod("getPostsByUserId", Long.class, Long.class);
        org.springframework.web.bind.annotation.GetMapping getPostsByUserIdMapping =
            getPostsByUserIdMethod.getAnnotation(org.springframework.web.bind.annotation.GetMapping.class);
        assertArrayEquals(new String[]{"/user/{userId}"}, getPostsByUserIdMapping.value());

        // getLikedPostsByUserId 方法
        Method getLikedPostsByUserIdMethod = PostController.class.getDeclaredMethod("getLikedPostsByUserId", Long.class, Long.class);
        org.springframework.web.bind.annotation.GetMapping getLikedPostsByUserIdMapping =
            getLikedPostsByUserIdMethod.getAnnotation(org.springframework.web.bind.annotation.GetMapping.class);
        assertArrayEquals(new String[]{"/liked/{userId}"}, getLikedPostsByUserIdMapping.value());

        // getFavoritePostsByUserId 方法
        Method getFavoritePostsByUserIdMethod = PostController.class.getDeclaredMethod("getFavoritePostsByUserId", Long.class, Long.class);
        org.springframework.web.bind.annotation.GetMapping getFavoritePostsByUserIdMapping =
            getFavoritePostsByUserIdMethod.getAnnotation(org.springframework.web.bind.annotation.GetMapping.class);
        assertArrayEquals(new String[]{"/favorite/{userId}"}, getFavoritePostsByUserIdMapping.value());
    }

    @Test
    void testDeleteMappingAnnotationDetails() throws NoSuchMethodException {
        // 验证 DeleteMapping 注解的详细信息
        Method deletePostMethod = PostController.class.getDeclaredMethod("deletePost", Long.class, Long.class);
        org.springframework.web.bind.annotation.DeleteMapping deleteMapping =
            deletePostMethod.getAnnotation(org.springframework.web.bind.annotation.DeleteMapping.class);

        // 检查 value 属性
        assertArrayEquals(new String[]{"/{postId}"}, deleteMapping.value());
    }

    @Test
    void testControllerInheritance() {
        // 验证控制器没有继承其他类（除了 Object）
        Class<?> superClass = PostController.class.getSuperclass();
        assertEquals(Object.class, superClass);
    }

    @Test
    void testControllerImplementsNoInterfaces() {
        // 验证控制器没有实现任何接口
        Class<?>[] interfaces = PostController.class.getInterfaces();
        assertEquals(0, interfaces.length);
    }
}
