package org.tourlink.routingservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tourlink.common.dto.dataPlatformDTO.UserProfileDTO;

/**
 * 数据中台服务Feign客户端
 * 用于调用数据中台服务的API
 */
@FeignClient(name = "data-platform-service", path = "/data-platform", url = "http://localhost:9081")
public interface DataPlatformClient {

    @GetMapping("/user-profile")
    UserProfileDTO getUserProfile(@RequestParam("userId") String userId);

}
