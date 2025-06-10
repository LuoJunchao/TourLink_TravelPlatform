package org.tourlink.common.dto.socialDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// 请求 DTO (提交评论用)
@Data
public class CommentRequest {

    @NotNull(message = "博客 ID 不能为空")
    private Long blogId;

    @NotBlank(message = "评论内容不能为空")
    private String content;

}
