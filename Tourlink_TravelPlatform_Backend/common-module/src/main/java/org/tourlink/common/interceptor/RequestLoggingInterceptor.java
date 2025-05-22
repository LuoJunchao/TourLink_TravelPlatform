package org.tourlink.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Enumeration;

/**
 * 请求日志拦截器
 * 用于记录请求日志
 */
@Slf4j
public class RequestLoggingInterceptor implements HandlerInterceptor {
    
    /**
     * 在请求处理之前调用
     * @param request 请求
     * @param response 响应
     * @param handler 处理器
     * @return 是否继续执行
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        String remoteAddr = request.getRemoteAddr();
        
        log.info("收到请求: {} {} 来自 {}", method, requestURI, remoteAddr);
        
        if (log.isDebugEnabled()) {
            logRequestHeaders(request);
        }
        
        return true;
    }
    
    /**
     * 在请求处理之后，视图渲染之前调用
     * @param request 请求
     * @param response 响应
     * @param handler 处理器
     * @param modelAndView 模型和视图
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        // 不做任何处理
    }
    
    /**
     * 在整个请求结束之后调用
     * @param request 请求
     * @param response 响应
     * @param handler 处理器
     * @param ex 异常
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        int status = response.getStatus();
        
        if (ex != null) {
            log.error("请求: {} {} 处理异常, 状态码: {}, 耗时: {}ms, 异常: {}", method, requestURI, status, executionTime, ex.getMessage(), ex);
        } else {
            log.info("请求: {} {} 处理完成, 状态码: {}, 耗时: {}ms", method, requestURI, status, executionTime);
        }
    }
    
    /**
     * 记录请求头
     * @param request 请求
     */
    private void logRequestHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        StringBuilder headers = new StringBuilder();
        headers.append("请求头: {");
        
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headers.append(headerName).append("=").append(headerValue);
            
            if (headerNames.hasMoreElements()) {
                headers.append(", ");
            }
        }
        
        headers.append("}");
        log.debug(headers.toString());
    }
}
