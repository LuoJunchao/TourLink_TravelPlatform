package org.tourlink.attractionservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.tourlink.attractionservice.repository.AttractionRepository;
import org.tourlink.attractionservice.entity.Attraction;
import org.tourlink.attractionservice.service.AttractionTagService;
import org.tourlink.common.dto.attractionDTO.AttractionTagsDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 景点标签服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AttractionTagServiceImpl implements AttractionTagService {
    
    private final AttractionRepository attractionRepository;
    
    @Override
    public AttractionTagsDTO getAttractionTags(Long attractionId) {
        Attraction attraction = attractionRepository.findById(attractionId)
                .orElseThrow(() -> new RuntimeException("景点不存在，ID: " + attractionId));
        
        return new AttractionTagsDTO(
                attraction.getAttractionId(),
                attraction.getName(),
                attraction.getTags()
        );
    }
    
    @Override
    public List<AttractionTagsDTO> getBatchAttractionTags(List<Long> attractionIds) {
        List<Attraction> attractions = attractionRepository.findAllById(attractionIds);
        
        return attractions.stream()
                .map(attraction -> new AttractionTagsDTO(
                        attraction.getAttractionId(),
                        attraction.getName(),
                        attraction.getTags()
                ))
                .collect(Collectors.toList());
    }
}