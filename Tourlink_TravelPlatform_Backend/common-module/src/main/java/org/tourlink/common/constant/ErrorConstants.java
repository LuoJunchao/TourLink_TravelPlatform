package org.tourlink.common.constant;

/**
 * 错误常量
 */
public class ErrorConstants {
    
    // 通用错误
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    public static final String BAD_REQUEST = "BAD_REQUEST";
    public static final String UNAUTHORIZED = "UNAUTHORIZED";
    public static final String FORBIDDEN = "FORBIDDEN";
    public static final String NOT_FOUND = "NOT_FOUND";
    public static final String METHOD_NOT_ALLOWED = "METHOD_NOT_ALLOWED";
    public static final String CONFLICT = "CONFLICT";
    
    // 验证错误
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    public static final String INVALID_PARAMETER = "INVALID_PARAMETER";
    public static final String MISSING_PARAMETER = "MISSING_PARAMETER";
    
    // 资源错误
    public static final String RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";
    public static final String RESOURCE_ALREADY_EXISTS = "RESOURCE_ALREADY_EXISTS";
    public static final String RESOURCE_OPERATION_FAILED = "RESOURCE_OPERATION_FAILED";
    
    // 用户相关错误
    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    public static final String USER_ALREADY_EXISTS = "USER_ALREADY_EXISTS";
    public static final String INVALID_CREDENTIALS = "INVALID_CREDENTIALS";
    public static final String ACCOUNT_LOCKED = "ACCOUNT_LOCKED";
    public static final String ACCOUNT_DISABLED = "ACCOUNT_DISABLED";
    
    // 权限相关错误
    public static final String ACCESS_DENIED = "ACCESS_DENIED";
    public static final String INVALID_TOKEN = "INVALID_TOKEN";
    public static final String TOKEN_EXPIRED = "TOKEN_EXPIRED";
    
    // 文件相关错误
    public static final String FILE_UPLOAD_FAILED = "FILE_UPLOAD_FAILED";
    public static final String FILE_TOO_LARGE = "FILE_TOO_LARGE";
    public static final String INVALID_FILE_TYPE = "INVALID_FILE_TYPE";
    
    private ErrorConstants() {
        throw new IllegalStateException("Utility class");
    }
}
