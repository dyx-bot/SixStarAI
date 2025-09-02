package com.example.appbacked.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CorsConfigTest {

    @Autowired
    private CorsConfig corsConfig;

    @Test
    void testCorsConfigBeanExists() {
        assertNotNull(corsConfig);
        assertTrue(corsConfig instanceof WebMvcConfigurer);
    }

    @Test
    void testAddCorsMappingsMethodExists() {
        try {
            Method method = corsConfig.getClass().getMethod("addCorsMappings", CorsRegistry.class);
            assertNotNull(method);
        } catch (NoSuchMethodException e) {
            fail("addCorsMappings method should exist");
        }
    }
}
