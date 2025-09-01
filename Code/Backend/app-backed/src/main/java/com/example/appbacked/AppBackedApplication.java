
// 文件: src/main/java/com/example/appbacked/AppBackedApplication.java
package com.example.appbacked;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Spring Boot 应用的主启动类
 */
@SpringBootApplication
public class AppBackedApplication {

    /**
     * 创建一个 WebClient Bean，用于调用外部 HTTP API（如 DeepSeek API）。
     * 这个 Bean 会被 Spring 容器管理，并可以被 @Autowired 注入到需要的地方（如 ChatService）。
     *
     * @return 配置好的 WebClient 实例
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                // 设置基础 URL，后续请求可以使用相对路径
                .baseUrl("https://api.deepseek.com/v1/chat/completions")
                // 设置默认请求头
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                // 可以添加其他配置，如超时、过滤器等
                .build();
    }

    /**
     * 应用程序的入口点。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(AppBackedApplication.class, args);
    }
}
