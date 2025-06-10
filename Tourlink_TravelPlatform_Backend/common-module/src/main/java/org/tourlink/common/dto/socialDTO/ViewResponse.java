package org.tourlink.common.dto.socialDTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ViewResponse {

    private Long blogId;
    private String userId;
    private int totalViews;
    private boolean isFirstView;
    private LocalDateTime viewTime;

}
