package org.tourlink.socialservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.tourlink.common.dto.AttractionTagsDTO;
import org.tourlink.common.response.ApiResponse;

import java.util.List;

/**
 * 景点服务Feign客户端
 * 用于调用景点服务的API
 */
@FeignClient(name = "attraction-service")
public interface AttractionClient {
    
    /**
     * 获取景点标签信息
     * @param id 景点ID
     * @return 景点标签信息
     */
    @GetMapping("/api/attractions/{id}/tags")
    ApiResponse<AttractionTagsDTO> getAttractionTags(@PathVariable("id") Long id);
    
    /**
     * 批量获取多个景点的标签信息
     * @param ids 景点ID列表，以逗号分隔
     * @return 多个景点的标签信息列表
     */
    @GetMapping("/api/attractions/batch/tags")
    ApiResponse<List<AttractionTagsDTO>> getBatchAttractionTags(@RequestParam("ids") String ids);
}
