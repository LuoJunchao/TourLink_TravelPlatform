package org.tourlink.common.dto.socialDTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BlogResponse {

    private Long blogId;
    private String userId;
    private String title;
    private String content;
    private List<String> images;
    private List<String> tags;
    private LocalDateTime publishTime;
    private Integer viewCount;
    private Integer likeCount;
    private Integer shareCount;
    private Integer commentCount;
    private double hotScore;

}
