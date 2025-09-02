package com.example.appbacked.controller;

import com.example.appbacked.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoginControllerTest {

    @Test
    void testLoginControllerLoads() {
        // 测试 LoginController 能正确加载
        LoginController controller = new LoginController();
        assertNotNull(controller);
    }

    @Test
    void testLoginControllerHasCorrectClassAnnotations() {
        // 验证类上有正确的注解
        assertTrue(LoginController.class.isAnnotationPresent(org.springframework.web.bind.annotation.CrossOrigin.class));
        assertTrue(LoginController.class.isAnnotationPresent(org.springframework.web.bind.annotation.RestController.class));
        assertTrue(LoginController.class.isAnnotationPresent(org.springframework.web.bind.annotation.RequestMapping.class));

        // 验证 RequestMapping 值
        org.springframework.web.bind.annotation.RequestMapping requestMapping =
            LoginController.class.getAnnotation(org.springframework.web.bind.annotation.RequestMapping.class);
        assertArrayEquals(new String[]{"/api"}, requestMapping.value());
    }

    @Test
    void testLoginControllerHasAutowiredFields() throws NoSuchFieldException {
        // 验证控制器有正确的 Autowired 字段
        Field userServiceField = LoginController.class.getDeclaredField("userService");
        assertNotNull(userServiceField);
        assertTrue(userServiceField.isAnnotationPresent(org.springframework.beans.factory.annotation.Autowired.class));
    }

    @Test
    void testLoginMethodExists() throws NoSuchMethodException {
        // 验证 login 方法存在且有正确的注解
        Method method = LoginController.class.getDeclaredMethod("login", User.class);
        assertNotNull(method);
        assertTrue(method.isAnnotationPresent(org.springframework.web.bind.annotation.PostMapping.class));

        org.springframework.web.bind.annotation.PostMapping annotation =
            method.getAnnotation(org.springframework.web.bind.annotation.PostMapping.class);
        assertArrayEquals(new String[]{"/login"}, annotation.value());
    }

    @Test
    void testLoginMethodReturnsCorrectType() throws NoSuchMethodException {
        // 验证 login 方法返回类型正确
        Method method = LoginController.class.getDeclaredMethod("login", User.class);
        assertEquals(java.util.Map.class, method.getReturnType());
    }

    @Test
    void testLoginMethodParameterAnnotations() throws NoSuchMethodException {
        // 验证 login 方法参数上有 RequestBody 注解
        Method loginMethod = LoginController.class.getDeclaredMethod("login", User.class);

        // 验证 @RequestBody 注解
        assertTrue(loginMethod.getParameters()[0].isAnnotationPresent(
            org.springframework.web.bind.annotation.RequestBody.class));
    }

    @Test
    void testCrossOriginAnnotation() {
        // 验证 CrossOrigin 注解的值
        org.springframework.web.bind.annotation.CrossOrigin crossOrigin =
            LoginController.class.getAnnotation(org.springframework.web.bind.annotation.CrossOrigin.class);

        // 检查 origins 属性
        String[] origins = crossOrigin.origins();
        assertNotNull(origins);
        // 根据实际的 @CrossOrigin 注解值进行调整
        if (origins.length > 0) {
            assertEquals("http://localhost:*", origins[0]);
        }

        // 检查 allowCredentials 属性（如果设置了的话）
        String allowCredentials = crossOrigin.allowCredentials();
        if (!allowCredentials.isEmpty()) {
            assertEquals("true", allowCredentials);
        }
    }

    @Test
    void testNoArgsConstructor() {
        // 测试 LoginController 有默认构造函数
        try {
            LoginController controller = LoginController.class.newInstance();
            assertNotNull(controller);
        } catch (InstantiationException | IllegalAccessException e) {
            fail("LoginController should have a no-args constructor");
        }
    }

    @Test
    void testLoginMethodStructure() throws NoSuchMethodException {
        // 验证 login 方法的结构
        Method loginMethod = LoginController.class.getDeclaredMethod("login", User.class);

        // 检查方法是 public 的
        assertTrue(java.lang.reflect.Modifier.isPublic(loginMethod.getModifiers()));

        // 检查返回类型是 Map<String, Object>
        assertEquals(java.util.Map.class, loginMethod.getReturnType());

        // 检查参数类型
        Class<?>[] parameterTypes = loginMethod.getParameterTypes();
        assertEquals(1, parameterTypes.length);
        assertEquals(User.class, parameterTypes[0]);
    }

    @Test
    void testLoginControllerClassStructure() {
        // 验证 LoginController 类的结构
        Class<?> clazz = LoginController.class;

        // 检查类是 public 的
        assertTrue(java.lang.reflect.Modifier.isPublic(clazz.getModifiers()));

        // 检查类不是 abstract 的
        assertFalse(java.lang.reflect.Modifier.isAbstract(clazz.getModifiers()));

        // 检查类不是 interface
        assertFalse(clazz.isInterface());
    }

    @Test
    void testAutowiredFieldStructure() throws NoSuchFieldException {
        // 验证 Autowired 字段的结构
        Field userServiceField = LoginController.class.getDeclaredField("userService");

        // 检查字段是 private 的
        assertTrue(java.lang.reflect.Modifier.isPrivate(userServiceField.getModifiers()));

        // 检查字段类型
        assertEquals(com.example.appbacked.service.UserService.class, userServiceField.getType());
    }

    @Test
    void testPostMappingAnnotationDetails() throws NoSuchMethodException {
        // 验证 PostMapping 注解的详细信息
        Method loginMethod = LoginController.class.getDeclaredMethod("login", User.class);
        org.springframework.web.bind.annotation.PostMapping postMapping =
            loginMethod.getAnnotation(org.springframework.web.bind.annotation.PostMapping.class);

        // 检查 value 属性
        assertArrayEquals(new String[]{"/login"}, postMapping.value());

        // 检查 path 属性（path 和 value 是别名，应该有相同的值）
        String[] pathValues = postMapping.path();
        if (pathValues.length > 0) {
            assertArrayEquals(new String[]{"/login"}, pathValues);
        }
    }

    @Test
    void testRequestMappingAnnotationDetails() {
        // 验证 RequestMapping 注解的详细信息
        org.springframework.web.bind.annotation.RequestMapping requestMapping =
            LoginController.class.getAnnotation(org.springframework.web.bind.annotation.RequestMapping.class);

        // 检查 value 属性
        assertArrayEquals(new String[]{"/api"}, requestMapping.value());

        // 检查 path 属性（path 和 value 是别名，可能为空数组）
        String[] pathValues = requestMapping.path();
        if (pathValues.length > 0) {
            assertArrayEquals(new String[]{"/api"}, pathValues);
        }
    }

    @Test
    void testControllerInheritance() {
        // 验证控制器没有继承其他类（除了 Object）
        Class<?> superClass = LoginController.class.getSuperclass();
        assertEquals(Object.class, superClass);
    }

    @Test
    void testControllerImplementsNoInterfaces() {
        // 验证控制器没有实现任何接口
        Class<?>[] interfaces = LoginController.class.getInterfaces();
        assertEquals(0, interfaces.length);
    }
}
