package org.tourlink.socialservice.dto;

import lombok.Data;
import org.tourlink.socialservice.entity.Blog;

import java.time.LocalDateTime;

@Data
public class BlogSummary {

    private Long blogId;
    private String title;
    private String userId;
    private LocalDateTime publishTime;
    private Integer likeCount;
    private Integer viewCount;
    private Integer commentCount;

    public static BlogSummary convertToSummary(Blog blog) {
        BlogSummary summary = new BlogSummary();

        summary.setBlogId(blog.getBlogId());
        summary.setTitle(blog.getTitle());
        summary.setUserId(blog.getUserId());
        summary.setPublishTime(blog.getPublishTime());
        summary.setLikeCount(blog.getLikeCount());
        summary.setViewCount(blog.getViewCount());
        summary.setCommentCount(blog.getCommentCount());

        return summary;
    }

}
