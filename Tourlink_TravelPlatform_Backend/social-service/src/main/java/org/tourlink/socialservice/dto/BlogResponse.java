package org.tourlink.socialservice.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BlogResponse {

    private Long blogId;
    private String userId;
    private String content;
    private List<String> images;
    private List<String> tags;
    private LocalDateTime publishTime;
    private Integer viewCount;
    private Integer likeCount;
    private Integer shareCount;
    private Integer commentCount;

    // 可选：作者信息（从 user-service 拉取）
    private String userName;
    private String userAvatar;

}
