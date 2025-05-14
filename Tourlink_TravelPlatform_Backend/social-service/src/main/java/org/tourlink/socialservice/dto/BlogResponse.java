package org.tourlink.socialservice.dto;

import lombok.Data;
import org.tourlink.socialservice.entity.Blog;

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

    public static BlogResponse convertToResponse(Blog blog) {
        BlogResponse blogResponse = new BlogResponse();

        blogResponse.setBlogId(blog.getBlogId());
        blogResponse.setUserId(blog.getUserId());
        blogResponse.setTitle(blog.getTitle());
        blogResponse.setContent(blog.getContent());
        blogResponse.setPublishTime(blog.getPublishTime());
        blogResponse.setViewCount(blog.getViewCount());
        blogResponse.setLikeCount(blog.getLikeCount());
        blogResponse.setCommentCount(blog.getCommentCount());
        blogResponse.setShareCount(blog.getShareCount());
        blogResponse.setHotScore(blog.getHotScore());

        List<String> tagNames = blog.getBlogTags().stream()
                .map(blogTag -> blogTag.getTag().getName())
                .toList();
        blogResponse.setTags(tagNames);

        return blogResponse;
    }

}
