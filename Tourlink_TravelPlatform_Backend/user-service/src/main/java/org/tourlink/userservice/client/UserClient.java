package org.tourlink.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.tourlink.common.dto.userDTO.UserBasicDTO;
import org.tourlink.common.response.ApiResponse;

import java.util.List;

/**
 * 用户服务Feign客户端接口示例
 * 其他服务可以参考此接口来创建自己的Feign客户端
 * 用于调用用户服务的API
 */
@FeignClient(name = "user-service")
public interface UserClient {
    
    /**
     * 获取用户基本信息
     * @param id 用户ID
     * @return 用户基本信息
     */
    @GetMapping("/api/users/{id}/basic")
    ApiResponse<UserBasicDTO> getUserBasicInfo(@PathVariable("id") Long id);
    
    /**
     * 批量获取多个用户的基本信息
     * @param ids 用户ID列表，以逗号分隔
     * @return 多个用户的基本信息列表
     */
    @GetMapping("/api/users/batch/basic")
    ApiResponse<List<UserBasicDTO>> getBatchUserBasicInfo(@RequestParam("ids") String ids);
}
