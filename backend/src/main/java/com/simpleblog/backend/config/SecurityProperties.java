package com.simpleblog.backend.config;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security")
public record SecurityProperties(
        String jwtSecret,
        long jwtExpirationSeconds,
        List<String> allowedOrigins
) {
}
