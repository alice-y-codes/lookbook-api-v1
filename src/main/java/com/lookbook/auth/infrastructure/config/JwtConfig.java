package com.lookbook.auth.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "application.security.jwt")
public class JwtConfig {
    private String secretKey;
    private long expiration;
    private RefreshToken refreshToken = new RefreshToken();

    @Data
    public static class RefreshToken {
        private long expiration;
    }
}