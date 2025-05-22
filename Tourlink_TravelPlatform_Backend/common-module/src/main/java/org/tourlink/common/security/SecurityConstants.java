package org.tourlink.common.security;

/**
 * 安全相关常量
 */
public class SecurityConstants {
    
    // JWT相关
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";
    
    // 角色前缀
    public static final String ROLE_PREFIX = "ROLE_";
    
    // 角色
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";
    
    // 权限
    public static final String PERM_USER_READ = "user:read";
    public static final String PERM_USER_WRITE = "user:write";
    public static final String PERM_ATTRACTION_READ = "attraction:read";
    public static final String PERM_ATTRACTION_WRITE = "attraction:write";
    public static final String PERM_BLOG_READ = "blog:read";
    public static final String PERM_BLOG_WRITE = "blog:write";
    
    // 安全相关端点
    public static final String LOGIN_ENDPOINT = "/api/auth/login";
    public static final String REGISTER_ENDPOINT = "/api/auth/register";
    public static final String REFRESH_TOKEN_ENDPOINT = "/api/auth/refresh";
    
    // 公开端点
    public static final String[] PUBLIC_ENDPOINTS = {
            LOGIN_ENDPOINT,
            REGISTER_ENDPOINT,
            "/api/attractions/**",
            "/api/blogs/public/**",
            "/actuator/health",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };
    
    private SecurityConstants() {
        throw new IllegalStateException("Utility class");
    }
}
