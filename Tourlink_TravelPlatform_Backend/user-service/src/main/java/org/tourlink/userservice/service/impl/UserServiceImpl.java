package org.tourlink.userservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tourlink.common.dto.userDTO.UserBasicDTO;
import org.tourlink.common.security.JwtPayload;
import org.tourlink.common.security.JwtUtils;
import org.tourlink.userservice.config.JwtConfig;
import org.tourlink.userservice.dto.AuthResponse;
import org.tourlink.userservice.dto.LoginRequest;
import org.tourlink.userservice.dto.RegisterRequest;
import org.tourlink.userservice.dto.UserRequest;
import org.tourlink.userservice.dto.UserUpdateRequest;
import org.tourlink.userservice.entity.User;
import org.tourlink.userservice.repository.UserRepository;
import org.tourlink.userservice.service.UserService;
import org.tourlink.userservice.util.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtConfig jwtConfig;

    @Override
    @Transactional
    public User createUser(UserRequest userRequest) {
        // 检查用户名和邮箱是否已存在
        if (userRepository.findByUsername(userRequest.getUsername()) != null) {
            throw new IllegalArgumentException("用户名已存在");
        }

        if (userRequest.getEmail() != null && userRepository.findByEmail(userRequest.getEmail()) != null) {
            throw new IllegalArgumentException("邮箱已存在");
        }

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setIsActive(true);
        user.setViewCount(0);
        user.setBlogIds(new ArrayList<>());
        user.setFavoriteAttractionIds(new ArrayList<>());
        user.setReviewIds(new ArrayList<>());

        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在，ID: " + id));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User updateUser(Long id, UserUpdateRequest userUpdateRequest) {
        User existingUser = getUserById(id);

        // 如果用户名变更，检查是否已存在
        if (!existingUser.getUsername().equals(userUpdateRequest.getUsername())) {
            User userWithSameUsername = userRepository.findByUsername(userUpdateRequest.getUsername());
            if (userWithSameUsername != null && !userWithSameUsername.getId().equals(id)) {
                throw new IllegalArgumentException("用户名已存在");
            }
            existingUser.setUsername(userUpdateRequest.getUsername());
        }

        // 如果邮箱变更，检查是否已存在
        if (userUpdateRequest.getEmail() != null && !userUpdateRequest.getEmail().equals(existingUser.getEmail())) {
            User userWithSameEmail = userRepository.findByEmail(userUpdateRequest.getEmail());
            if (userWithSameEmail != null && !userWithSameEmail.getId().equals(id)) {
                throw new IllegalArgumentException("邮箱已存在");
            }
            existingUser.setEmail(userUpdateRequest.getEmail());
        }

        // 更新手机号
        existingUser.setPhoneNumber(userUpdateRequest.getPhoneNumber());
        existingUser.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("用户不存在，ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public List<Long> getUserBlogs(Long userId) {
        User user = getUserById(userId);
        return user.getBlogIds() != null ? user.getBlogIds() : new ArrayList<>();
    }

    @Override
    public List<Long> getUserFavorites(Long userId) {
        User user = getUserById(userId);
        return user.getFavoriteAttractionIds() != null ? user.getFavoriteAttractionIds() : new ArrayList<>();
    }

    @Override
    public List<Long> getUserReviews(Long userId) {
        User user = getUserById(userId);
        return user.getReviewIds() != null ? user.getReviewIds() : new ArrayList<>();
    }

    @Override
    @Transactional
    public void addFavorite(Long userId, Long attractionId) {
        User user = getUserById(userId);
        List<Long> favorites = user.getFavoriteAttractionIds();
        if (favorites == null) {
            favorites = new ArrayList<>();
            user.setFavoriteAttractionIds(favorites);
        }

        if (!favorites.contains(attractionId)) {
            favorites.add(attractionId);
            userRepository.save(user);
        }
    }

    @Override
    @Transactional
    public void removeFavorite(Long userId, Long attractionId) {
        User user = getUserById(userId);
        List<Long> favorites = user.getFavoriteAttractionIds();
        if (favorites != null) {
            favorites.remove(attractionId);
            userRepository.save(user);
        }
    }

    @Override
    @Transactional
    public void incrementViewCount(Long userId) {
        User user = getUserById(userId);
        user.setViewCount(user.getViewCount() + 1);
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserBasicDTO getUserBasicDTO(Long id) {
        User user = getUserById(id);
        return convertToUserBasicDTO(user);
    }

    @Override
    public List<UserBasicDTO> getBatchUserBasicDTO(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }

        return ids.stream()
                .map(id -> {
                    try {
                        return getUserBasicDTO(id);
                    } catch (EntityNotFoundException e) {
                        return null;
                    }
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }

    /**
     * 将User实体转换为UserBasicDTO
     * @param user 用户实体
     * @return 用户基本信息DTO
     */
    private UserBasicDTO convertToUserBasicDTO(User user) {
        if (user == null) {
            return null;
        }

        UserBasicDTO dto = new UserBasicDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        // 注意：这里的avatarUrl需要从用户的profile中获取，如果有的话
        // 目前暂时设置为null，后续可以完善
        dto.setAvatarUrl(null);

        return dto;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        // 检查用户名和邮箱是否已存在
        if (userRepository.findByUsername(registerRequest.getUsername()) != null) {
            throw new IllegalArgumentException("用户名已存在");
        }

        if (registerRequest.getEmail() != null && userRepository.findByEmail(registerRequest.getEmail()) != null) {
            throw new IllegalArgumentException("邮箱已存在");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setIsActive(true);
        user.setViewCount(0);
        user.setBlogIds(new ArrayList<>());
        user.setFavoriteAttractionIds(new ArrayList<>());
        user.setReviewIds(new ArrayList<>());

        // 保存用户
        User savedUser = userRepository.save(user);

        // 生成JWT令牌
        return generateAuthResponse(savedUser);
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        // 根据用户名查找用户
        User user = userRepository.findByUsername(loginRequest.getUsername());
        if (user == null) {
            throw new BadCredentialsException("用户名或密码不正确");
        }

        // 验证密码
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("用户名或密码不正确");
        }

        // 检查用户是否激活
        if (!user.getIsActive()) {
            throw new BadCredentialsException("账户已被禁用");
        }

        // 生成JWT令牌
        return generateAuthResponse(user);
    }

    /**
     * 生成认证响应
     * @param user 用户实体
     * @return 认证响应
     */
    private AuthResponse generateAuthResponse(User user) {
        // 创建JWT载荷
        JwtPayload payload = JwtPayload.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .roles(Collections.singletonList("ROLE_USER"))
                .build();

        // 生成JWT令牌
        String token = JwtUtils.generateToken(payload, jwtConfig.getSecret());

        // 创建认证响应
        return AuthResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .token(token)
                .roles(Collections.singletonList("ROLE_USER"))
                .expiresIn(jwtConfig.getExpiration())
                .build();
    }
}
