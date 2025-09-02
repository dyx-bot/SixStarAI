package com.example.appbacked.controller;

import com.example.appbacked.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminControllerTest {

    @Test
    void testAdminControllerLoads() {
        // 测试 AdminController 能正确加载
        AdminController controller = new AdminController();
        assertNotNull(controller);
    }

    @Test
    void testAdminControllerHasCorrectClassAnnotations() {
        // 验证类上有正确的注解
        assertTrue(AdminController.class.isAnnotationPresent(org.springframework.web.bind.annotation.RestController.class));
        assertTrue(AdminController.class.isAnnotationPresent(org.springframework.web.bind.annotation.CrossOrigin.class));
        assertTrue(AdminController.class.isAnnotationPresent(org.springframework.web.bind.annotation.RequestMapping.class));

        // 验证 RequestMapping 值
        org.springframework.web.bind.annotation.RequestMapping requestMapping =
            AdminController.class.getAnnotation(org.springframework.web.bind.annotation.RequestMapping.class);
        assertArrayEquals(new String[]{"/api/admin"}, requestMapping.value());
    }

    @Test
    void testAdminControllerHasAutowiredFields() throws NoSuchFieldException {
        // 验证控制器有正确的 Autowired 字段
        Field userServiceField = AdminController.class.getDeclaredField("userService");
        Field userRepositoryField = AdminController.class.getDeclaredField("userRepository");
        Field postRepositoryField = AdminController.class.getDeclaredField("postRepository");

        assertNotNull(userServiceField);
        assertNotNull(userRepositoryField);
        assertNotNull(postRepositoryField);

        assertTrue(userServiceField.isAnnotationPresent(org.springframework.beans.factory.annotation.Autowired.class));
        assertTrue(userRepositoryField.isAnnotationPresent(org.springframework.beans.factory.annotation.Autowired.class));
        assertTrue(postRepositoryField.isAnnotationPresent(org.springframework.beans.factory.annotation.Autowired.class));
    }

    @Test
    void testAdminLoginMethodExists() throws NoSuchMethodException {
        // 验证 adminLogin 方法存在且有正确的注解
        Method method = AdminController.class.getDeclaredMethod("adminLogin", User.class);

        assertNotNull(method);
        assertTrue(method.isAnnotationPresent(org.springframework.web.bind.annotation.PostMapping.class));

        org.springframework.web.bind.annotation.PostMapping annotation =
            method.getAnnotation(org.springframework.web.bind.annotation.PostMapping.class);
        assertArrayEquals(new String[]{"/login"}, annotation.value());
    }

    @Test
    void testGetAllUsersMethodExists() throws NoSuchMethodException {
        // 验证 getAllUsers 方法存在且有正确的注解
        Method method = AdminController.class.getDeclaredMethod("getAllUsers");

        assertNotNull(method);
        assertTrue(method.isAnnotationPresent(org.springframework.web.bind.annotation.GetMapping.class));

        org.springframework.web.bind.annotation.GetMapping annotation =
            method.getAnnotation(org.springframework.web.bind.annotation.GetMapping.class);
        assertArrayEquals(new String[]{"/users"}, annotation.value());
    }

    @Test
    void testUpdateUserRoleMethodExists() throws NoSuchMethodException {
        // 验证 updateUserRole 方法存在且有正确的注解
        Method method = AdminController.class.getDeclaredMethod("updateUserRole", Long.class, Map.class);

        assertNotNull(method);
        assertTrue(method.isAnnotationPresent(org.springframework.web.bind.annotation.PutMapping.class));

        org.springframework.web.bind.annotation.PutMapping annotation =
            method.getAnnotation(org.springframework.web.bind.annotation.PutMapping.class);
        assertArrayEquals(new String[]{"/users/{id}/role"}, annotation.value());
    }

    @Test
    void testGetStatsMethodExists() throws NoSuchMethodException {
        // 验证 getStats 方法存在且有正确的注解
        Method method = AdminController.class.getDeclaredMethod("getStats");

        assertNotNull(method);
        assertTrue(method.isAnnotationPresent(org.springframework.web.bind.annotation.GetMapping.class));

        org.springframework.web.bind.annotation.GetMapping annotation =
            method.getAnnotation(org.springframework.web.bind.annotation.GetMapping.class);
        assertArrayEquals(new String[]{"/stats"}, annotation.value());
    }

    @Test
    void testGetGradeStatsMethodExists() throws NoSuchMethodException {
        // 验证 getGradeStats 方法存在且有正确的注解
        Method method = AdminController.class.getDeclaredMethod("getGradeStats");

        assertNotNull(method);
        assertTrue(method.isAnnotationPresent(org.springframework.web.bind.annotation.GetMapping.class));

        org.springframework.web.bind.annotation.GetMapping annotation =
            method.getAnnotation(org.springframework.web.bind.annotation.GetMapping.class);
        assertArrayEquals(new String[]{"/stats/grades"}, annotation.value());
    }

    @Test
    void testGetCollegeStatsMethodExists() throws NoSuchMethodException {
        // 验证 getCollegeStats 方法存在且有正确的注解
        Method method = AdminController.class.getDeclaredMethod("getCollegeStats");

        assertNotNull(method);
        assertTrue(method.isAnnotationPresent(org.springframework.web.bind.annotation.GetMapping.class));

        org.springframework.web.bind.annotation.GetMapping annotation =
            method.getAnnotation(org.springframework.web.bind.annotation.GetMapping.class);
        assertArrayEquals(new String[]{"/stats/colleges"}, annotation.value());
    }

    @Test
    void testGetTagStatsMethodExists() throws NoSuchMethodException {
        // 验证 getTagStats 方法存在且有正确的注解
        Method method = AdminController.class.getDeclaredMethod("getTagStats");

        assertNotNull(method);
        assertTrue(method.isAnnotationPresent(org.springframework.web.bind.annotation.GetMapping.class));

        org.springframework.web.bind.annotation.GetMapping annotation =
            method.getAnnotation(org.springframework.web.bind.annotation.GetMapping.class);
        assertArrayEquals(new String[]{"/stats/tags"}, annotation.value());
    }

    @Test
    void testGetAllPostsMethodExists() throws NoSuchMethodException {
        // 验证 getAllPosts 方法存在且有正确的注解
        Method method = AdminController.class.getDeclaredMethod("getAllPosts", int.class, int.class);

        assertNotNull(method);
        assertTrue(method.isAnnotationPresent(org.springframework.web.bind.annotation.GetMapping.class));

        org.springframework.web.bind.annotation.GetMapping annotation =
            method.getAnnotation(org.springframework.web.bind.annotation.GetMapping.class);
        assertArrayEquals(new String[]{"/posts"}, annotation.value());
    }

    @Test
    void testDeletePostMethodExists() throws NoSuchMethodException {
        // 验证 deletePost 方法存在且有正确的注解
        Method method = AdminController.class.getDeclaredMethod("deletePost", Long.class);

        assertNotNull(method);
        assertTrue(method.isAnnotationPresent(org.springframework.web.bind.annotation.DeleteMapping.class));

        org.springframework.web.bind.annotation.DeleteMapping annotation =
            method.getAnnotation(org.springframework.web.bind.annotation.DeleteMapping.class);
        assertArrayEquals(new String[]{"/posts/{id}"}, annotation.value());
    }

    @Test
    void testGetAllUsersWithPaginationMethodExists() throws NoSuchMethodException {
        // 验证 getAllUsersWithPagination 方法存在且有正确的注解
        Method method = AdminController.class.getDeclaredMethod("getAllUsersWithPagination", int.class, int.class);

        assertNotNull(method);
        assertTrue(method.isAnnotationPresent(org.springframework.web.bind.annotation.GetMapping.class));

        org.springframework.web.bind.annotation.GetMapping annotation =
            method.getAnnotation(org.springframework.web.bind.annotation.GetMapping.class);
        assertArrayEquals(new String[]{"/users/page"}, annotation.value());
    }

    @Test
    void testDeleteUserMethodExists() throws NoSuchMethodException {
        // 验证 deleteUser 方法存在且有正确的注解
        Method method = AdminController.class.getDeclaredMethod("deleteUser", Long.class);

        assertNotNull(method);
        assertTrue(method.isAnnotationPresent(org.springframework.web.bind.annotation.DeleteMapping.class));

        org.springframework.web.bind.annotation.DeleteMapping annotation =
            method.getAnnotation(org.springframework.web.bind.annotation.DeleteMapping.class);
        assertArrayEquals(new String[]{"/users/{id}"}, annotation.value());
    }

    @Test
    void testCreateUserMethodExists() throws NoSuchMethodException {
        // 验证 createUser 方法存在且有正确的注解
        Method method = AdminController.class.getDeclaredMethod("createUser", User.class);

        assertNotNull(method);
        assertTrue(method.isAnnotationPresent(org.springframework.web.bind.annotation.PostMapping.class));

        org.springframework.web.bind.annotation.PostMapping annotation =
            method.getAnnotation(org.springframework.web.bind.annotation.PostMapping.class);
        assertArrayEquals(new String[]{"/users"}, annotation.value());
    }

    @Test
    void testHashWithSHA256MethodExists() throws NoSuchMethodException {
        // 验证 hashWithSHA256 私有方法存在
        Method method = AdminController.class.getDeclaredMethod("hashWithSHA256", String.class);
        assertNotNull(method);
    }

    @Test
    void testHashWithSHA256Functionality() throws Exception {
        // 测试 SHA-256 加密功能
        AdminController controller = new AdminController();
        java.lang.reflect.Method method = AdminController.class.getDeclaredMethod("hashWithSHA256", String.class);
        method.setAccessible(true);

        String testInput = "test123";
        String hashedOutput = (String) method.invoke(controller, testInput);

        assertNotNull(hashedOutput);
        assertEquals(64, hashedOutput.length()); // SHA-256 produces 64-character hex string
    }

    @Test
    void testHashWithSHA256ConsistentResults() throws Exception {
        // 测试相同输入产生相同输出
        AdminController controller = new AdminController();
        java.lang.reflect.Method method = AdminController.class.getDeclaredMethod("hashWithSHA256", String.class);
        method.setAccessible(true);

        String testInput = "password123";
        String result1 = (String) method.invoke(controller, testInput);
        String result2 = (String) method.invoke(controller, testInput);

        assertEquals(result1, result2);
    }

    @Test
    void testHashWithSHA256DifferentInputsProduceDifferentOutputs() throws Exception {
        // 测试不同输入产生不同输出
        AdminController controller = new AdminController();
        java.lang.reflect.Method method = AdminController.class.getDeclaredMethod("hashWithSHA256", String.class);
        method.setAccessible(true);

        String input1 = "password1";
        String input2 = "password2";
        String result1 = (String) method.invoke(controller, input1);
        String result2 = (String) method.invoke(controller, input2);

        assertNotEquals(result1, result2);
    }

    @Test
    void testMethodReturnTypes() throws NoSuchMethodException {
        // 验证所有方法返回类型正确
        Method adminLoginMethod = AdminController.class.getDeclaredMethod("adminLogin", User.class);
        Method getAllUsersMethod = AdminController.class.getDeclaredMethod("getAllUsers");
        Method updateUserRoleMethod = AdminController.class.getDeclaredMethod("updateUserRole", Long.class, Map.class);
        Method getStatsMethod = AdminController.class.getDeclaredMethod("getStats");
        Method getGradeStatsMethod = AdminController.class.getDeclaredMethod("getGradeStats");
        Method getCollegeStatsMethod = AdminController.class.getDeclaredMethod("getCollegeStats");
        Method getTagStatsMethod = AdminController.class.getDeclaredMethod("getTagStats");
        Method getAllPostsMethod = AdminController.class.getDeclaredMethod("getAllPosts", int.class, int.class);
        Method deletePostMethod = AdminController.class.getDeclaredMethod("deletePost", Long.class);
        Method getAllUsersWithPaginationMethod = AdminController.class.getDeclaredMethod("getAllUsersWithPagination", int.class, int.class);
        Method deleteUserMethod = AdminController.class.getDeclaredMethod("deleteUser", Long.class);
        Method createUserMethod = AdminController.class.getDeclaredMethod("createUser", User.class);

        assertEquals(org.springframework.http.ResponseEntity.class, adminLoginMethod.getReturnType());
        assertEquals(org.springframework.http.ResponseEntity.class, getAllUsersMethod.getReturnType());
        assertEquals(org.springframework.http.ResponseEntity.class, updateUserRoleMethod.getReturnType());
        assertEquals(org.springframework.http.ResponseEntity.class, getStatsMethod.getReturnType());
        assertEquals(org.springframework.http.ResponseEntity.class, getGradeStatsMethod.getReturnType());
        assertEquals(org.springframework.http.ResponseEntity.class, getCollegeStatsMethod.getReturnType());
        assertEquals(org.springframework.http.ResponseEntity.class, getTagStatsMethod.getReturnType());
        assertEquals(org.springframework.http.ResponseEntity.class, getAllPostsMethod.getReturnType());
        assertEquals(org.springframework.http.ResponseEntity.class, deletePostMethod.getReturnType());
        assertEquals(org.springframework.http.ResponseEntity.class, getAllUsersWithPaginationMethod.getReturnType());
        assertEquals(org.springframework.http.ResponseEntity.class, deleteUserMethod.getReturnType());
        assertEquals(org.springframework.http.ResponseEntity.class, createUserMethod.getReturnType());
    }

    @Test
    void testMethodParameterAnnotations() throws NoSuchMethodException {
        // 验证方法参数上的注解
        Method adminLoginMethod = AdminController.class.getDeclaredMethod("adminLogin", User.class);
        Method updateUserRoleMethod = AdminController.class.getDeclaredMethod("updateUserRole", Long.class, Map.class);
        Method getAllPostsMethod = AdminController.class.getDeclaredMethod("getAllPosts", int.class, int.class);
        Method getAllUsersWithPaginationMethod = AdminController.class.getDeclaredMethod("getAllUsersWithPagination", int.class, int.class);
        Method deletePostMethod = AdminController.class.getDeclaredMethod("deletePost", Long.class);
        Method deleteUserMethod = AdminController.class.getDeclaredMethod("deleteUser", Long.class);
        Method createUserMethod = AdminController.class.getDeclaredMethod("createUser", User.class);

        // 验证 @RequestBody 注解
        assertTrue(adminLoginMethod.getParameters()[0].isAnnotationPresent(
            org.springframework.web.bind.annotation.RequestBody.class));
        assertTrue(updateUserRoleMethod.getParameters()[1].isAnnotationPresent(
            org.springframework.web.bind.annotation.RequestBody.class));
        assertTrue(createUserMethod.getParameters()[0].isAnnotationPresent(
            org.springframework.web.bind.annotation.RequestBody.class));

        // 验证 @PathVariable 注解
        assertTrue(updateUserRoleMethod.getParameters()[0].isAnnotationPresent(
            org.springframework.web.bind.annotation.PathVariable.class));
        assertTrue(deletePostMethod.getParameters()[0].isAnnotationPresent(
            org.springframework.web.bind.annotation.PathVariable.class));
        assertTrue(deleteUserMethod.getParameters()[0].isAnnotationPresent(
            org.springframework.web.bind.annotation.PathVariable.class));

        // 验证 @RequestParam 注解
        assertTrue(getAllPostsMethod.getParameters()[0].isAnnotationPresent(
            org.springframework.web.bind.annotation.RequestParam.class));
        assertTrue(getAllPostsMethod.getParameters()[1].isAnnotationPresent(
            org.springframework.web.bind.annotation.RequestParam.class));
        assertTrue(getAllUsersWithPaginationMethod.getParameters()[0].isAnnotationPresent(
            org.springframework.web.bind.annotation.RequestParam.class));
        assertTrue(getAllUsersWithPaginationMethod.getParameters()[1].isAnnotationPresent(
            org.springframework.web.bind.annotation.RequestParam.class));
    }

    @Test
    void testRequestParamDefaultValues() throws NoSuchMethodException {
        // 验证 @RequestParam 的默认值
        Method getAllPostsMethod = AdminController.class.getDeclaredMethod("getAllPosts", int.class, int.class);
        org.springframework.web.bind.annotation.RequestParam pageParam =
            getAllPostsMethod.getParameters()[0].getAnnotation(org.springframework.web.bind.annotation.RequestParam.class);
        org.springframework.web.bind.annotation.RequestParam sizeParam =
            getAllPostsMethod.getParameters()[1].getAnnotation(org.springframework.web.bind.annotation.RequestParam.class);

        assertEquals("0", pageParam.defaultValue());
        assertEquals("10", sizeParam.defaultValue());

        Method getAllUsersWithPaginationMethod = AdminController.class.getDeclaredMethod("getAllUsersWithPagination", int.class, int.class);
        org.springframework.web.bind.annotation.RequestParam pageParam2 =
            getAllUsersWithPaginationMethod.getParameters()[0].getAnnotation(org.springframework.web.bind.annotation.RequestParam.class);
        org.springframework.web.bind.annotation.RequestParam sizeParam2 =
            getAllUsersWithPaginationMethod.getParameters()[1].getAnnotation(org.springframework.web.bind.annotation.RequestParam.class);

        assertEquals("0", pageParam2.defaultValue());
        assertEquals("10", sizeParam2.defaultValue());
    }

    @Test
    void testNoArgsConstructor() {
        // 测试 AdminController 有默认构造函数
        try {
            AdminController controller = AdminController.class.newInstance();
            assertNotNull(controller);
        } catch (InstantiationException | IllegalAccessException e) {
            fail("AdminController should have a no-args constructor");
        }
    }

    @Test
    void testCrossOriginAnnotation() {
        // 验证 CrossOrigin 注解的值
        org.springframework.web.bind.annotation.CrossOrigin crossOrigin =
            AdminController.class.getAnnotation(org.springframework.web.bind.annotation.CrossOrigin.class);

        assertArrayEquals(new String[]{"http://localhost:3000", "http://localhost:8080"}, crossOrigin.origins());
        assertEquals("true", crossOrigin.allowCredentials());
    }
}
