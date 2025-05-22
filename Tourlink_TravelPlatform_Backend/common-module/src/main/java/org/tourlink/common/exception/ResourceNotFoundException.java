package org.tourlink.common.exception;

/**
 * 资源不存在异常
 */
public class ResourceNotFoundException extends BusinessException {
    
    private static final String ERROR_CODE = "RESOURCE_NOT_FOUND";
    
    public ResourceNotFoundException(String resourceType, Long resourceId) {
        super(ERROR_CODE, String.format("%s with id %d not found", resourceType, resourceId));
    }
    
    public ResourceNotFoundException(String resourceType, String resourceIdentifier) {
        super(ERROR_CODE, String.format("%s with identifier %s not found", resourceType, resourceIdentifier));
    }
    
    public ResourceNotFoundException(String message) {
        super(ERROR_CODE, message);
    }
}
