package org.tourlink.common.exception;

import lombok.Getter;

/**
 * 业务异常基类，所有业务异常都应该继承此类
 */
@Getter
public class BusinessException extends RuntimeException {
    private final String errorCode;
    private final String errorMessage;
    
    public BusinessException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    
    public BusinessException(String errorCode, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
