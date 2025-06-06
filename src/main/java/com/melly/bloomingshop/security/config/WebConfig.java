package com.melly.bloomingshop.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 외부 디렉토리 매핑
        registry.addResourceHandler("/images/**") // URL 패턴 설정
                .addResourceLocations("file:///C:/bloomingshop/product") // 외부 디렉토리 경로 매핑
                .addResourceLocations("file:///C:/bloomingshop/support") // 외부 디렉토리 경로 매핑
                .addResourceLocations("file:///home/ubuntu/bloomingshop/product/")  // 서버 디렉터리 경로
                .addResourceLocations("file:///home/ubuntu/bloomingshop/support/");

        // 정적 리소스 (static 폴더) 매핑
        registry.addResourceHandler("/static/**") // URL 패턴 설정
                .addResourceLocations("classpath:/static/images"); // classpath의 static 폴더 매핑
    }
}
