package org.tourlink.socialservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tourlink.common.dto.socialDTO.ChatPreview;
import org.tourlink.common.dto.socialDTO.MessageRequest;
import org.tourlink.common.dto.socialDTO.MessageResponse;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    // 发送私信
    @PostMapping
    public ResponseEntity<Void> sendMessage(@RequestBody MessageRequest request) {
        return null;
    }

    // 获取与某用户的私信记录
    @GetMapping("/chat")
    public ResponseEntity<List<MessageResponse>> getChat(
            @RequestParam String userId,
            @RequestParam String targetUserId
    ) {
        return null;
    }

    // 获取用户所有私信对话对象
    @GetMapping("/dialogs/{userId}")
    public ResponseEntity<List<ChatPreview>> getChatList(@PathVariable String userId) {
        return null;
    }
}
