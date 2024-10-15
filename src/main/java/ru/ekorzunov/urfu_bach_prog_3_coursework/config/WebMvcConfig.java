package ru.ekorzunov.urfu_bach_prog_3_coursework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public LoggingInterceptor loggingInterceptor() {
        return new LoggingInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor()).addPathPatterns("/users/update/save");
        registry.addInterceptor(loggingInterceptor()).addPathPatterns("/register/save");
        registry.addInterceptor(loggingInterceptor()).addPathPatterns("/logout");

        registry.addInterceptor(loggingInterceptor()).addPathPatterns("/autos/save");
        registry.addInterceptor(loggingInterceptor()).addPathPatterns("/autos/delete");

        registry.addInterceptor(loggingInterceptor()).addPathPatterns("/rentrecords/save");
        registry.addInterceptor(loggingInterceptor()).addPathPatterns("/rentrecords/delete");
    }
}
