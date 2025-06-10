package org.tourlink.common.dto.socialDTO;

import lombok.Data;

import java.time.LocalDateTime;

// 相应 DTO (返回给前端)
@Data
public class CommentResponse {

    private Long commentId;

    private String userId;

    private String content;

    private LocalDateTime commentTime;

}
