package org.tourlink.common.dto.dataPlatformDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProfileDTO {

    private String userId;

    private List<String> topTags;

    private Map<String, Double> tagWeights;

    private Map<String, LocalDateTime> tagUpdateTimes;

    public UserProfileDTO() {}

    // 显式添加全参构造器
    public UserProfileDTO(String userId, List<String> topTags,
                          Map<String, Double> tagWeights,
                          Map<String, LocalDateTime> tagUpdateTimes) {
        this.userId = userId;
        this.topTags = topTags;
        this.tagWeights = tagWeights;
        this.tagUpdateTimes = tagUpdateTimes;
    }

}
