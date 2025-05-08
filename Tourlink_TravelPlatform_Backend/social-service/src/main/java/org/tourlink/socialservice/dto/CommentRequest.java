package org.tourlink.socialservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// 请求 DTO (提交评论用)
@Data
public class CommentRequest {

    @NotNull
    private Long blogId;

    @NotBlank
    private String content;

}
