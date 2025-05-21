package org.tourlink.userservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserProfileRequest {

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Size(max = 50, message = "昵称长度不能超过50个字符")
    private String nickname;

    @Size(max = 500, message = "个人简介长度不能超过500个字符")
    private String bio;

    @Size(max = 200, message = "头像URL长度不能超过200个字符")
    private String avatarUrl;

    private String gender;

    private String birthdate;

    @Size(max = 100, message = "所在地长度不能超过100个字符")
    private String location;
}
