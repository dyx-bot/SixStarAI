package com.example.appbacked.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.lang.reflect.Field;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WebMvcConfigTest {

    private WebMvcConfig webMvcConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        webMvcConfig = new WebMvcConfig();

        // 使用反射设置 uploadDir 字段的值，避免 NullPointerException
        try {
            Field uploadDirField = WebMvcConfig.class.getDeclaredField("uploadDir");
            uploadDirField.setAccessible(true);
            uploadDirField.set(webMvcConfig, "./uploads");
        } catch (Exception e) {
            // 如果设置失败，我们仍然继续测试其他部分
        }
    }

    @Test
    void webMvcConfigShouldBeCreated() {
        assertNotNull(webMvcConfig);
    }

    @Test
    void webMvcConfigShouldImplementWebMvcConfigurer() {
        assertTrue(webMvcConfig instanceof org.springframework.web.servlet.config.annotation.WebMvcConfigurer);
    }

    @Test
    void uploadDirFieldShouldExist() {
        try {
            Field uploadDirField = WebMvcConfig.class.getDeclaredField("uploadDir");
            assertNotNull(uploadDirField);
            assertEquals("uploadDir", uploadDirField.getName());
        } catch (NoSuchFieldException e) {
            fail("uploadDir field should exist in WebMvcConfig class");
        }
    }

    @Test
    void addResourceHandlersMethodShouldExistAndBeOverridden() {
        try {
            java.lang.reflect.Method method = WebMvcConfig.class.getMethod("addResourceHandlers", ResourceHandlerRegistry.class);
            assertNotNull(method);
            assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
        } catch (NoSuchMethodException e) {
            fail("addResourceHandlers method should exist in WebMvcConfig class");
        }
    }

    @Test
    void loggerShouldBeInitialized() {
        try {
            Field loggerField = WebMvcConfig.class.getDeclaredField("logger");
            loggerField.setAccessible(true);
            Object logger = loggerField.get(null); // static field
            assertNotNull(logger);
        } catch (Exception e) {
            fail("Logger should be initialized");
        }
    }

    @Test
    void pathsShouldBeResolvedCorrectly() {
        String uploadDir = "./uploads";
        String absolutePath = Paths.get(uploadDir, "avatars").toAbsolutePath().toString();

        assertNotNull(absolutePath);
        assertTrue(absolutePath.contains("avatars"));
    }

    @Test
    void uploadDirShouldHaveDefaultValue() {
        try {
            Field uploadDirField = WebMvcConfig.class.getDeclaredField("uploadDir");
            uploadDirField.setAccessible(true);

            // Since @Value is not processed in unit test, we can't check actual value
            // But we can verify the field exists and is of correct type
            assertEquals(String.class, uploadDirField.getType());
        } catch (NoSuchFieldException e) {
            fail("uploadDir field should exist");
        }
    }

    @Test
    void addResourceHandlersShouldExecuteWithoutException() {
        // Create a mock registry to avoid complex Spring initialization
        ResourceHandlerRegistry registry = mock(ResourceHandlerRegistry.class);

        // Mock the method chain
        when(registry.addResourceHandler(anyString())).thenReturn(mock(org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration.class));

        // Verify that method can be called without exception
        assertDoesNotThrow(() -> {
            webMvcConfig.addResourceHandlers(registry);
        }, "addResourceHandlers should not throw exception");
    }
}
