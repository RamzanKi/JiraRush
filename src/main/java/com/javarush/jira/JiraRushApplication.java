package com.javarush.jira;

import com.javarush.jira.common.config.AppProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
@EnableCaching
@AllArgsConstructor
public class JiraRushApplication implements WebMvcConfigurer {

    private final LocaleChangeInterceptor localeChangeInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        interceptorRegistry.addInterceptor(localeChangeInterceptor);
    }

    public static void main(String[] args) {
        SpringApplication.run(JiraRushApplication.class, args);
    }
}
