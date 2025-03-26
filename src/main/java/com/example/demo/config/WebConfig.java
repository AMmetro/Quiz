package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer { // резолверы аргументов, форматтеры данных, интерсепторы и т.д.

    @Autowired
    private CurrentUserIdArgumentResolver currentUserIdArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) { // регестрирует HandlerMethodArgumentResolver в spring
        resolvers.add(currentUserIdArgumentResolver); // внедрение резолвера в проект
    }
} 