package org.tourlink.common.dto.socialDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class BlogRequest {

    @NotBlank(message = "用户 ID 不能为空")
    private String userId;

    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题长度不能超过100个字符")
    private String title;

    @NotBlank(message = "内容不能为空")
    @Size(min = 10, max = 5000, message = "内容长度应在10-5000个字符之间")
    private String content;

    @Size(max = 9, message = "最多上传9张图片")
    private List<String> images;

    @Size(max = 5, message = "最多关联5个景点")
    private List<Long> attractionIds;

}
