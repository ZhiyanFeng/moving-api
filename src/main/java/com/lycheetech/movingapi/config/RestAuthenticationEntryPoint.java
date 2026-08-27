package com.lycheetech.movingapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lycheetech.movingapi.common.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Returns a JSON 401 response (instead of the default HTML/basic-auth challenge)
 * whenever an unauthenticated request hits a protected endpoint.
 * <p>
 * Uses its own dedicated (classic) Jackson 2 {@link ObjectMapper} instance rather
 * than an injected/autoconfigured bean, since Spring Boot's autoconfigured
 * ObjectMapper may be a Jackson 3 ({@code tools.jackson.databind.ObjectMapper})
 * instance depending on the Boot version, which is a different, incompatible type.
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                          HttpServletResponse response,
                          AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(
                objectMapper.writeValueAsString(ApiResponse.error("Missing or invalid authentication token")));
    }
}

