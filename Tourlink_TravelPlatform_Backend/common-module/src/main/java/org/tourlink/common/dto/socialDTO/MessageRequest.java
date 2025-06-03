package org.tourlink.common.dto.socialDTO;

import lombok.Data;

@Data
public class MessageRequest {

    private String senderId;
    private String receiverId;
    private String content;

}
