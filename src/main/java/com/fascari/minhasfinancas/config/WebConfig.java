package com.fascari.minhasfinancas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedMethods("HEAD,GET,POST,PUT,DELETE,PATCH,OPTIONS".split(",")).allowedHeaders(
                "Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method".split(","));

    }
}
