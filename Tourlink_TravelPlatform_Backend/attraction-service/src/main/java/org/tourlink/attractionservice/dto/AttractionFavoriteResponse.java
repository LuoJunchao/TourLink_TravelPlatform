package org.tourlink.attractionservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AttractionFavoriteResponse {
    private Long favoriteId;
    private Long attractionId;
    private Long userId;
    private LocalDateTime createdTime;
}