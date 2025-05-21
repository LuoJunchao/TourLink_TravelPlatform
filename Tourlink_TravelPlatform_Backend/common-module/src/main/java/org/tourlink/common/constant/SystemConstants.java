package org.tourlink.common.constant;

/**
 * 系统常量
 */
public class SystemConstants {
    
    // 分页默认值
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 100;
    
    // 缓存相关
    public static final String USER_CACHE_PREFIX = "user:";
    public static final String ATTRACTION_CACHE_PREFIX = "attraction:";
    public static final String BLOG_CACHE_PREFIX = "blog:";
    public static final String REVIEW_CACHE_PREFIX = "review:";
    
    // 文件上传相关
    public static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    public static final String ALLOWED_IMAGE_EXTENSIONS = "jpg,jpeg,png,gif,webp";
    public static final String ALLOWED_DOCUMENT_EXTENSIONS = "pdf,doc,docx,xls,xlsx,ppt,pptx";
    
    // 用户相关
    public static final int MIN_USERNAME_LENGTH = 3;
    public static final int MAX_USERNAME_LENGTH = 50;
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_PASSWORD_LENGTH = 100;
    
    // 内容相关
    public static final int MAX_BLOG_TITLE_LENGTH = 100;
    public static final int MAX_BLOG_CONTENT_LENGTH = 10000;
    public static final int MAX_COMMENT_LENGTH = 500;
    public static final int MAX_REVIEW_CONTENT_LENGTH = 1000;
    
    // 服务名称
    public static final String USER_SERVICE = "user-service";
    public static final String ATTRACTION_SERVICE = "attraction-service";
    public static final String SOCIAL_SERVICE = "social-service";
    public static final String ROUTING_SERVICE = "routing-service";
    public static final String DATA_PLATFORM_SERVICE = "data-platform-service";
    
    // API路径
    public static final String API_PREFIX = "/api";
    public static final String USER_API_PATH = API_PREFIX + "/users";
    public static final String ATTRACTION_API_PATH = API_PREFIX + "/attractions";
    public static final String BLOG_API_PATH = API_PREFIX + "/blogs";
    public static final String REVIEW_API_PATH = API_PREFIX + "/reviews";
    
    private SystemConstants() {
        throw new IllegalStateException("Utility class");
    }
}
