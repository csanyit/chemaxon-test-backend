package com.chemaxon.homework.csanyit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Optional;

@SpringBootApplication
public class HomeworkApplication {

  private static final String CORS_ALLOW_ORIGIN = Optional.ofNullable(System.getenv("CORS_ALLOW_ORIGIN")).orElse("*");

  public static void main(String... args) {
    SpringApplication.run(HomeworkApplication.class, args);
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurerAdapter() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(CORS_ALLOW_ORIGIN);
      }
    };
  }

}
