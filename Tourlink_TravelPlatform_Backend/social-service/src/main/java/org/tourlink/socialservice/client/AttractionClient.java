package org.tourlink.socialservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.tourlink.common.dto.attractionDTO.AttractionTagsDTO;
import org.tourlink.common.response.ApiResponse;

import java.util.List;

/**
 * 景点服务Feign客户端
 * 用于调用社交服务的API
 */
@FeignClient(name = "attraction-service", path = "/api/attractions")
public interface AttractionClient {

    /**
     * 获取某个景点的标签信息
     * @param attractionId 景点ID
     * @return 景点标签信息
     */
    @GetMapping("/{attractionId}")
    ApiResponse<AttractionTagsDTO> getAttractionTags(@PathVariable("attractionId") Long attractionId);

    /**
     * 批量获取多个景点的标签信息
     * @param ids 逗号分隔的景点ID字符串
     * @return 多个景点的标签信息列表
     */
    @GetMapping("/batch")
    ApiResponse<List<AttractionTagsDTO>> getBatchAttractionTags(@RequestParam("ids") String ids);

}
