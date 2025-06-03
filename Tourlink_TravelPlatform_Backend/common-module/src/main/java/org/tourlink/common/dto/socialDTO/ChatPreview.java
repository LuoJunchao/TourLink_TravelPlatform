package org.tourlink.common.dto.socialDTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatPreview {

    private String userId; // 对话对方的 ID
    private String userName;
    private String userAvatar;
    private String lastMessage; // 最后一条消息内容
    private LocalDateTime lastMessageTime;
    private Boolean hasUnread; // 是否还有未读消息

}
