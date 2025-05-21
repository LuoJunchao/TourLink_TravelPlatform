package org.tourlink.socialservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageResponse {

    private Long messageId;
    private String senderId;
    private String senderName;
    private String receiverId;
    private String receiverName;
    private String content;
    private LocalDateTime sendTime;
    private Boolean isRead;

}
