package org.tourlink.attractionservice.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tourlink.attractionservice.dto.AttractionRequest;
import org.tourlink.attractionservice.dto.AttractionResponse;
import org.tourlink.attractionservice.dto.AttractionSummary;
import org.tourlink.attractionservice.entity.Attraction;
import org.tourlink.attractionservice.service.AttractionService;
import org.tourlink.common.dto.attractionDTO.AttractionTagsDTO;
import org.tourlink.common.response.ApiResponse;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/attractions")
public class AttractionController {

    @Autowired
    private AttractionService attractionService;

    @GetMapping
    public ResponseEntity<List<AttractionSummary>> getAllAttractions() {
        return ResponseEntity.ok(attractionService.getAllAttractions());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<AttractionSummary>> getAttractionsPaged(
            @PageableDefault(sort = "attractionId", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(attractionService.getAttractionsPaged(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttractionResponse> getAttractionById(@PathVariable Long id) {
        // 增加浏览量
        attractionService.incrementViewCount(id);
        return ResponseEntity.ok(attractionService.getAttractionById(id));
    }

    @PostMapping
    public ResponseEntity<AttractionResponse> createAttraction(@Valid @RequestBody AttractionRequest request) {
        return ResponseEntity.ok(attractionService.createAttraction(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttractionResponse> updateAttraction(
            @PathVariable Long id,
            @Valid @RequestBody AttractionRequest request) {
        return ResponseEntity.ok(attractionService.updateAttraction(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttraction(@PathVariable Long id) {
        attractionService.deleteAttraction(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<AttractionSummary>> searchAttractions(
            @RequestParam String keyword,
            @PageableDefault(sort = "attractionId", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(attractionService.searchAttractions(keyword, pageable));
    }

    @GetMapping("/featured")
    public ResponseEntity<List<AttractionSummary>> getFeaturedAttractions() {
        return ResponseEntity.ok(attractionService.getFeaturedAttractions());
    }

    @GetMapping("/top-rated")
    public ResponseEntity<Page<AttractionSummary>> getTopRatedAttractions(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(attractionService.getTopRatedAttractions(pageable));
    }

    @GetMapping("/most-viewed")
    public ResponseEntity<Page<AttractionSummary>> getMostViewedAttractions(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(attractionService.getMostViewedAttractions(pageable));
    }

    /**
     * 获取景点标签信息
     * 该接口专为微服务间通信设计，返回景点的标签信息
     * @param id 景点ID
     * @return 景点标签信息
     */
    @GetMapping("/{id}/tags")
    public ResponseEntity<ApiResponse<AttractionTagsDTO>> getAttractionTags(@PathVariable Long id) {
        Attraction attraction = attractionService.getAttractionEntityById(id);

        AttractionTagsDTO tagsDTO = new AttractionTagsDTO();
        tagsDTO.setAttractionId(attraction.getAttractionId());
        tagsDTO.setAttractionName(attraction.getName());
        tagsDTO.setTags(attraction.getTags());

        return ResponseEntity.ok(ApiResponse.success(tagsDTO));
    }

    /**
     * 批量获取多个景点的标签信息
     * @param ids 景点ID列表，以逗号分隔
     * @return 多个景点的标签信息列表
     */
    @GetMapping("/batch/tags")
    public ResponseEntity<ApiResponse<List<AttractionTagsDTO>>> getBatchAttractionTags(@RequestParam String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .toList();

        List<AttractionTagsDTO> tagsDTOList = idList.stream()
                .map(id -> {
                    try {
                        Attraction attraction = attractionService.getAttractionEntityById(id);
                        AttractionTagsDTO tagsDTO = new AttractionTagsDTO();
                        tagsDTO.setAttractionId(attraction.getAttractionId());
                        tagsDTO.setAttractionName(attraction.getName());
                        tagsDTO.setTags(attraction.getTags());
                        return tagsDTO;
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(tagsDTOList));
    }
}