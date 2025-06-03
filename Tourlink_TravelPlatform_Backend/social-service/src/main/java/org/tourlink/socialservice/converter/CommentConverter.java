package org.tourlink.socialservice.converter;

import org.tourlink.common.dto.socialDTO.CommentResponse;
import org.tourlink.socialservice.entity.BlogComment;

public class CommentConverter {

    public static CommentResponse toResponse(BlogComment blogComment) {
        CommentResponse commentResponse = new CommentResponse();

        commentResponse.setCommentId(blogComment.getCommentId());
        commentResponse.setUserId(blogComment.getUserId());
        commentResponse.setContent(blogComment.getContent());
        commentResponse.setCommentTime(blogComment.getCommentTime());

        return commentResponse;
    }

}
