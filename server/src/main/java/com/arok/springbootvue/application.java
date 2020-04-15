package com.arok.springbootvue;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;
import java.util.stream.Stream;

@SpringBootApplication
public class application {
    public static void main(String[] args) {
        SpringApplication.run(application.class, args);
    }

    // test data
    @Bean
    ApplicationRunner init(TodoRepository repository) {
        return args -> {
            Stream.of("Study spring", "Eat pizza", "Study Vue.js", "Study algorithm").forEach(name -> {
                Todo todo = new Todo();
                todo.setTitle(name);
                repository.save(todo);
            });
            repository.findAll().forEach(System.out::println);
        };
    }

    // fix the CORS errors
    @Bean
    public FilterRegistrationBean simpleCorsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        /* URL below needs to match the Vue client URL and port */
        config.setAllowedOrigins(Collections.singletonList("http://localhost:8080"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
