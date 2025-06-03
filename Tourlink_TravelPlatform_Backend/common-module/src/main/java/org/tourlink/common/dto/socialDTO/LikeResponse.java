package org.tourlink.common.dto.socialDTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LikeResponse {
    private Long blogId;
    private String userId;
    private boolean liked; // true 表示点赞成功，false 表示取消点赞
    private LocalDateTime likeTime;

}
