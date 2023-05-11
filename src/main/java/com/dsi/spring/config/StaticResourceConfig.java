package com.dsi.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AutoConfigureAfter(DispatcherServletAutoConfiguration.class)
public class StaticResourceConfig implements WebMvcConfigurer {

    @Value("${static.resource.path}")
    String staticResourcePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**").addResourceLocations(staticResourcePath + "/static/images/");
    }
}
