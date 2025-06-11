package org.tourlink.userservice.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tourlink.common.dto.userDTO.UserBasicDTO;
import org.tourlink.common.response.ApiResponse;
import org.tourlink.userservice.dto.UserRequest;
import org.tourlink.userservice.dto.UserResponse;
import org.tourlink.userservice.dto.UserUpdateRequest;
import org.tourlink.userservice.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(UserResponse.fromEntity(userService.createUser(userRequest)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(UserResponse.fromEntity(userService.getUserById(id)));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers().stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        return ResponseEntity.ok(UserResponse.fromEntity(userService.updateUser(id, userUpdateRequest)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/blogs")
    public ResponseEntity<List<Long>> getUserBlogs(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserBlogs(id));
    }

    @GetMapping("/{id}/favorites")
    public ResponseEntity<List<Long>> getUserFavorites(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserFavorites(id));
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<Long>> getUserReviews(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserReviews(id));
    }

    @PostMapping("/{id}/favorites/{attractionId}")
    public ResponseEntity<Void> addFavorite(@PathVariable Long id, @PathVariable Long attractionId) {
        userService.addFavorite(id, attractionId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/favorites/{attractionId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable Long id, @PathVariable Long attractionId) {
        userService.removeFavorite(id, attractionId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/view")
    public ResponseEntity<Void> incrementViewCount(@PathVariable Long id) {
        userService.incrementViewCount(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 获取用户基本信息DTO
     * @param id 用户ID
     * @return 用户基本信息DTO
     */
    @GetMapping("/{id}/basic")
    public ResponseEntity<ApiResponse<UserBasicDTO>> getUserBasicInfo(@PathVariable Long id) {
        UserBasicDTO userBasicDTO = userService.getUserBasicDTO(id);
        return ResponseEntity.ok(ApiResponse.success(userBasicDTO));
    }

    /**
     * 批量获取多个用户的基本信息DTO
     * @param ids 用户ID列表，以逗号分隔
     * @return 多个用户的基本信息DTO列表
     */
    @GetMapping("/batch/basic")
    public ResponseEntity<ApiResponse<List<UserBasicDTO>>> getBatchUserBasicInfo(@RequestParam String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .collect(Collectors.toList());

        List<UserBasicDTO> userBasicDTOList = userService.getBatchUserBasicDTO(idList);
        return ResponseEntity.ok(ApiResponse.success(userBasicDTOList));
    }
}
