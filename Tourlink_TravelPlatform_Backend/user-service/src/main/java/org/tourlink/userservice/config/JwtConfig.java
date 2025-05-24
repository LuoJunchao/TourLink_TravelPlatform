package org.tourlink.userservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * JWT配置类
 */
@Configuration
public class JwtConfig {
    
    @Value("${jwt.secret:TourlinkSecretKey123456789012345678901234567890}")
    private String secret;
    
    @Value("${jwt.expiration:86400000}") // 默认24小时
    private long expiration;
    
    public String getSecret() {
        return secret;
    }
    
    public long getExpiration() {
        return expiration;
    }
}
