package com.juliovillalvazo.todoappapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class TodoAppApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoAppApiApplication.class, args);
    }


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/todos").allowedOrigins("http://localhost:5173");
                registry.addMapping("/todos/{id}").allowedOrigins("http://localhost:5173");
                registry.addMapping("/todos/{id}/done").allowedOrigins("http://localhost:5173");
                registry.addMapping("/todos/{id}/undone").allowedOrigins("http://localhost:5173");

            }
        };
    }

}

