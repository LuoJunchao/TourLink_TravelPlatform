package org.tourlink.userservice.service;

import org.tourlink.common.dto.userDTO.UserBasicDTO;
import org.tourlink.userservice.dto.AuthResponse;
import org.tourlink.userservice.dto.LoginRequest;
import org.tourlink.userservice.dto.RegisterRequest;
import org.tourlink.userservice.dto.UserRequest;
import org.tourlink.userservice.dto.UserUpdateRequest;
import org.tourlink.userservice.entity.User;
import java.util.List;

public interface UserService {
    User createUser(UserRequest userRequest);
    User getUserById(Long id);
    List<User> getAllUsers();
    User updateUser(Long id, UserUpdateRequest userUpdateRequest);
    void deleteUser(Long id);
    List<Long> getUserBlogs(Long userId);
    List<Long> getUserFavorites(Long userId);
    List<Long> getUserReviews(Long userId);
    void addFavorite(Long userId, Long attractionId);
    void removeFavorite(Long userId, Long attractionId);
    void incrementViewCount(Long userId);
    User findByUsername(String username);
    User findByEmail(String email);

    /**
     * 获取用户基本信息DTO
     * @param id 用户ID
     * @return 用户基本信息DTO
     */
    UserBasicDTO getUserBasicDTO(Long id);

    /**
     * 批量获取多个用户的基本信息DTO
     * @param ids 用户ID列表
     * @return 多个用户的基本信息DTO列表
     */
    List<UserBasicDTO> getBatchUserBasicDTO(List<Long> ids);

    /**
     * 用户注册
     * @param registerRequest 注册请求
     * @return 认证响应
     */
    AuthResponse register(RegisterRequest registerRequest);

    /**
     * 用户登录
     * @param loginRequest 登录请求
     * @return 认证响应
     */
    AuthResponse login(LoginRequest loginRequest);
}
