package org.tourlink.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 景点标签DTO，用于微服务间传递景点标签信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttractionTagsDTO {
    private Long attractionId;
    private String attractionName;
    private List<String> tags;
}
