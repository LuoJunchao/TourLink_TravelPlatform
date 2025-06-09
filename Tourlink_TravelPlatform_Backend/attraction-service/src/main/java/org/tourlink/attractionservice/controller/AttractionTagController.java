package org.tourlink.attractionservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tourlink.common.dto.AttractionTagsDTO;
import org.tourlink.common.response.ApiResponse;
import org.tourlink.attractionservice.service.AttractionTagService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 景点标签控制器
 */
@RestController
@RequestMapping("/api/attractions/tags")
@RequiredArgsConstructor
public class AttractionTagController {

    private final AttractionTagService attractionTagService;

    /**
     * 获取景点标签信息
     * @param attractionId 景点ID
     * @return 景点标签信息
     */
    @GetMapping("/{attractionId}")
    public ResponseEntity<ApiResponse<AttractionTagsDTO>> getAttractionTags(@PathVariable Long attractionId) {
        AttractionTagsDTO tagsDTO = attractionTagService.getAttractionTags(attractionId);
        return ResponseEntity.ok(ApiResponse.success(tagsDTO));
    }

    /**
     * 批量获取多个景点的标签信息
     * @param ids 景点ID列表，以逗号分隔
     * @return 多个景点的标签信息列表
     */
    @GetMapping("/batch")
    public ResponseEntity<ApiResponse<List<AttractionTagsDTO>>> getBatchAttractionTags(@RequestParam String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        
        List<AttractionTagsDTO> tagsDTOList = attractionTagService.getBatchAttractionTags(idList);
        return ResponseEntity.ok(ApiResponse.success(tagsDTOList));
    }
}