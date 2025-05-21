package org.tourlink.common.exception;

/**
 * 未授权异常
 */
public class UnauthorizedException extends BusinessException {
    
    private static final String ERROR_CODE = "UNAUTHORIZED";
    
    public UnauthorizedException() {
        super(ERROR_CODE, "未授权的访问");
    }
    
    public UnauthorizedException(String message) {
        super(ERROR_CODE, message);
    }
}
