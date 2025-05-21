package org.tourlink.socialservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.tourlink.common.dto.AttractionTagsDTO;
import org.tourlink.common.response.ApiResponse;
import org.tourlink.socialservice.client.AttractionClient;
import org.tourlink.socialservice.service.AttractionTagService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 景点标签服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AttractionTagServiceImpl implements AttractionTagService {

    private final AttractionClient attractionClient;

    @Override
    public AttractionTagsDTO getAttractionTags(Long attractionId) {
        try {
            ApiResponse<AttractionTagsDTO> response = attractionClient.getAttractionTags(attractionId);
            if (response != null && response.isSuccess() && response.getData() != null) {
                return response.getData();
            }
            log.warn("获取景点标签失败，景点ID: {}", attractionId);
            return new AttractionTagsDTO(attractionId, null, Collections.emptyList());
        } catch (Exception e) {
            log.error("调用景点服务获取标签异常，景点ID: {}", attractionId, e);
            return new AttractionTagsDTO(attractionId, null, Collections.emptyList());
        }
    }

    @Override
    public List<AttractionTagsDTO> getBatchAttractionTags(List<Long> attractionIds) {
        if (attractionIds == null || attractionIds.isEmpty()) {
            return Collections.emptyList();
        }
        
        try {
            // 将ID列表转换为逗号分隔的字符串
            String ids = attractionIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            
            ApiResponse<List<AttractionTagsDTO>> response = attractionClient.getBatchAttractionTags(ids);
            if (response != null && response.isSuccess() && response.getData() != null) {
                return response.getData();
            }
            log.warn("批量获取景点标签失败，景点ID列表: {}", ids);
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("调用景点服务批量获取标签异常", e);
            return Collections.emptyList();
        }
    }
}
