package com.example.appbacked.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = WebClientConfig.class)
@TestPropertySource(properties = {
    "anything-llm.base-url=http://localhost:8080",
    "anything-llm.api-key=test-key"
})
class WebClientConfigTest {

    @Test
    void testWebClientConfigFieldsExist() throws NoSuchFieldException {
        // 测试字段是否存在
        Field baseUrlField = WebClientConfig.class.getDeclaredField("baseUrl");
        Field apiKeyField = WebClientConfig.class.getDeclaredField("apiKey");

        assertNotNull(baseUrlField);
        assertNotNull(apiKeyField);
    }

    @Test
    void testWebClientCreationWithHardcodedValues() {
        WebClientConfig config = new WebClientConfig();
        WebClient webClient = config.anythingLlmWebClient();

        // 验证 WebClient 创建成功
        assertNotNull(webClient);
    }

    @Test
    void testWebClientConfigIsConfiguration() {
        // 验证类上有正确的注解
        assertTrue(WebClientConfig.class.isAnnotationPresent(org.springframework.context.annotation.Configuration.class));
    }

    @Test
    void testAnythingLlmWebClientIsBean() throws NoSuchMethodException {
        // 验证方法上有 Bean 注解
        assertTrue(WebClientConfig.class.getDeclaredMethod("anythingLlmWebClient")
                  .isAnnotationPresent(org.springframework.context.annotation.Bean.class));
    }
}
