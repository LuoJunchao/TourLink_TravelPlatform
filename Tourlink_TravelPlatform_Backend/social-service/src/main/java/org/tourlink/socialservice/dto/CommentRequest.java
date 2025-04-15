package org.tourlink.socialservice.dto;

import lombok.Data;

@Data
public class CommentRequest {

    private Long blogId;
    private String userId;
    private String content;
    private Long parentCommentId;

}
