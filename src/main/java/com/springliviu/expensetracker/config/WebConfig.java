package com.springliviu.expensetracker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final DevUserArgumentResolver devUserArgumentResolver;

    @Autowired
    public WebConfig(DevUserArgumentResolver devUserArgumentResolver) {
        this.devUserArgumentResolver = devUserArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(devUserArgumentResolver);
    }
}
