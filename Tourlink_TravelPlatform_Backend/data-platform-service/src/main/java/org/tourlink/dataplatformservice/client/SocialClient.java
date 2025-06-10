package org.tourlink.dataplatformservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 社交服务Feign客户端
 * 用于调用社交服务的API
 */
@FeignClient(name = "social-service", path = "/api/social/blogs")
public interface SocialClient {

    @GetMapping("/{blogId}/tags")
    List<String> getBlogTags(@PathVariable("blogId") Long blogId);

}