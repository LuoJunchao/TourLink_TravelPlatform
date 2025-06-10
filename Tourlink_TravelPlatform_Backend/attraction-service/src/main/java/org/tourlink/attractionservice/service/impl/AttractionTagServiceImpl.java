package org.tourlink.attractionservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.tourlink.attractionservice.service.AttractionTagService;
import org.tourlink.common.dto.attractionDTO.AttractionTagsDTO;

import java.util.Collections;
import java.util.List;

/**
 * 景点标签服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AttractionTagServiceImpl implements AttractionTagService {

    // TODO: 注入相关的Repository
    
    @Override
    public AttractionTagsDTO getAttractionTags(Long attractionId) {
        // TODO: 实现获取单个景点标签的逻辑
        return new AttractionTagsDTO(attractionId, null, Collections.emptyList());
    }

    @Override
    public List<AttractionTagsDTO> getBatchAttractionTags(List<Long> attractionIds) {
        // TODO: 实现批量获取景点标签的逻辑
        return Collections.emptyList();
    }
}