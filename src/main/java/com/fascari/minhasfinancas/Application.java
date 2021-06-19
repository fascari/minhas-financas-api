package com.fascari.minhasfinancas;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@EnableWebMvc
public class Application implements WebMvcConfigurer {

    public static void main(String[] args) {
        run(Application.class, args);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("HEAD,GET,POST,PUT,DELETE,PATCH,OPTIONS".split(","))
                .allowedHeaders("Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method".split(","));

    }
}
