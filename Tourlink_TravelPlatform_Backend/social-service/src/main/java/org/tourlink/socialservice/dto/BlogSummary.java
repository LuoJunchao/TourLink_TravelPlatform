package org.tourlink.socialservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlogSummary {

    private Long blogId;
    private String title;
    private String userId;
    private LocalDateTime publishTime;
    private Integer likeCount;
    private Integer viewCount;

}
