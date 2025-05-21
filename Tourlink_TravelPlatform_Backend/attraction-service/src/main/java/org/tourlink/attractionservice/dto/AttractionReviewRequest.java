package org.tourlink.attractionservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AttractionReviewRequest {

    @NotNull(message = "景点ID不能为空")
    private Long attractionId;

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最小为1")
    @Max(value = 5, message = "评分最大为5")
    private BigDecimal rating;

    @NotBlank(message = "评论内容不能为空")
    @Size(min = 5, max = 1000, message = "评论内容长度应在5-1000个字符之间")
    private String content;

    private String title;
}
