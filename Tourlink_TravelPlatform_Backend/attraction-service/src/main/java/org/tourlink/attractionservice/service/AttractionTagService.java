package org.tourlink.attractionservice.service;

import org.tourlink.common.dto.AttractionTagsDTO;

import java.util.List;

/**
 * 景点标签服务接口
 */
public interface AttractionTagService {
    
    /**
     * 获取景点标签信息
     * @param attractionId 景点ID
     * @return 景点标签信息
     */
    AttractionTagsDTO getAttractionTags(Long attractionId);
    
    /**
     * 批量获取多个景点的标签信息
     * @param attractionIds 景点ID列表
     * @return 多个景点的标签信息列表
     */
    List<AttractionTagsDTO> getBatchAttractionTags(List<Long> attractionIds);
}