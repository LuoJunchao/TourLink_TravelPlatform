package org.tourlink.userservice.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tourlink.common.response.ApiResponse;
import org.tourlink.common.security.SecurityConstants;
import org.tourlink.userservice.dto.AuthResponse;
import org.tourlink.userservice.dto.LoginRequest;
import org.tourlink.userservice.dto.RegisterRequest;
import org.tourlink.userservice.service.UserService;

/**
 * 认证控制器
 * 处理用户登录和注册
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 用户注册
     * @param registerRequest 注册请求
     * @return 认证响应
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        AuthResponse authResponse = userService.register(registerRequest);
        return ResponseEntity.ok(ApiResponse.success(authResponse));
    }
    
    /**
     * 用户登录
     * @param loginRequest 登录请求
     * @return 认证响应
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = userService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.success(authResponse));
    }
}
