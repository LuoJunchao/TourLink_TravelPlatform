package org.tourlink.socialservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponse {

    private Long commentId;
    private String userId;
    private String userName; // 可选字段，用于前端显示用户昵称
    private String userAvatar; // 可选字段，用于显示用户头像
    private String content;
    private LocalDateTime commentTime;

}
