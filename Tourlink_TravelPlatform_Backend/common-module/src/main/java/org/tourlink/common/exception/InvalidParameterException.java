package org.tourlink.common.exception;

/**
 * 参数无效异常
 */
public class InvalidParameterException extends BusinessException {
    
    private static final String ERROR_CODE = "INVALID_PARAMETER";
    
    public InvalidParameterException(String paramName, String reason) {
        super(ERROR_CODE, String.format("Parameter '%s' is invalid: %s", paramName, reason));
    }
    
    public InvalidParameterException(String message) {
        super(ERROR_CODE, message);
    }
}
