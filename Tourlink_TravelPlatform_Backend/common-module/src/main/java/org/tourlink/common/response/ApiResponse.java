package org.tourlink.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一API响应格式
 * @param <T> 响应数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String code;
    private String message;
    private T data;
    
    /**
     * 成功响应
     * @param data 响应数据
     * @param <T> 数据类型
     * @return API响应对象
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "200", "操作成功", data);
    }
    
    /**
     * 成功响应（无数据）
     * @return API响应对象
     */
    public static ApiResponse<Void> success() {
        return new ApiResponse<>(true, "200", "操作成功", null);
    }
    
    /**
     * 成功响应（自定义消息）
     * @param message 成功消息
     * @param data 响应数据
     * @param <T> 数据类型
     * @return API响应对象
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, "200", message, data);
    }
    
    /**
     * 错误响应
     * @param code 错误代码
     * @param message 错误消息
     * @return API响应对象
     */
    public static <T> ApiResponse<T> error(String code, String message) {
        return new ApiResponse<>(false, code, message, null);
    }
    
    /**
     * 错误响应（带数据）
     * @param code 错误代码
     * @param message 错误消息
     * @param data 错误相关数据
     * @param <T> 数据类型
     * @return API响应对象
     */
    public static <T> ApiResponse<T> error(String code, String message, T data) {
        return new ApiResponse<>(false, code, message, data);
    }
}
