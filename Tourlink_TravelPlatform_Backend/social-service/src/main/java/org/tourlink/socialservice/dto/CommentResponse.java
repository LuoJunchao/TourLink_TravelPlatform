package org.tourlink.socialservice.dto;

import lombok.Data;
import org.tourlink.socialservice.entity.BlogComment;

import java.time.LocalDateTime;

// 相应 DTO (返回给前端)
@Data
public class CommentResponse {

    private Long commentId;

    private String userId;

    private String content;

    private LocalDateTime commentTime;

    public static CommentResponse fromBlogComment(BlogComment blogComment) {
        CommentResponse commentResponse = new CommentResponse();

        commentResponse.setCommentId(blogComment.getCommentId());
        commentResponse.setUserId(blogComment.getUserId());
        commentResponse.setContent(blogComment.getContent());
        commentResponse.setCommentTime(blogComment.getCommentTime());

        return commentResponse;
    }

}
