package com.lycheetech.movingapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        // Always respond with JSON, regardless of the client's Accept header
        // or any path/query-based format hints (e.g. .xml suffix).
        configurer
                .favorParameter(false)
                .ignoreAcceptHeader(true)
                .defaultContentType(MediaType.APPLICATION_JSON);
    }

    /**
     * Logs every incoming request (method, URI, query string, client, headers,
     * and body) to the console at DEBUG level. See the matching
     * logging.level.org.springframework.web.filter.CommonsRequestLoggingFilter
     * property in application.properties, which must be DEBUG for this to emit.
     */
    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludeClientInfo(true);
        filter.setIncludeHeaders(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setAfterMessagePrefix("REQUEST DATA : ");
        return filter;
    }
}

