package com.example.appbacked.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.file.Paths;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);

    @Value("${file.upload-dir:./uploads}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将 /uploads/avatars/** 映射到实际的文件目录
        String absolutePath = Paths.get(uploadDir, "avatars").toAbsolutePath().toString();

        registry.addResourceHandler("/uploads/avatars/**")
                .addResourceLocations("file:" + absolutePath + "/")
                .setCachePeriod(3600);

        logger.info("静态资源映射: /uploads/avatars/** -> file:{}/", absolutePath);
    }
}
