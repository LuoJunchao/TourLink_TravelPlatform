package org.tourlink.common.dto.dataPlatformDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileDTO {

    private String userId;

    private List<String> topTags;

    private Map<String, Double> tagWeights;

    private Map<String, LocalDateTime> tagUpdateTimes;
}
