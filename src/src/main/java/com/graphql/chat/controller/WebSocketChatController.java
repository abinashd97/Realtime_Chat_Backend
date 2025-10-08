package com.graphql.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.graphql.chat.dto.MessageDTO;
import com.graphql.chat.model.ChatRoom;
import com.graphql.chat.model.Message;
import com.graphql.chat.model.User;
import com.graphql.chat.service.ChatRoomService;
import com.graphql.chat.service.MessageService;
import com.graphql.chat.service.UserService;

import java.time.LocalDateTime;

@Controller
public class WebSocketChatController {

    private final UserService userService;
    private final ChatRoomService chatRoomService;
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketChatController(UserService userService,
                                   ChatRoomService chatRoomService,
                                   MessageService messageService,
                                   SimpMessagingTemplate messagingTemplate) {
        this.userService = userService;
        this.chatRoomService = chatRoomService;
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
    }

    // Client sends to /app/chat.sendMessage
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(MessageDTO dto) {
        User sender = userService.findById(dto.getSenderId()).orElse(null);
        ChatRoom room = chatRoomService.findById(dto.getRoomId()).orElse(null);
        if (sender == null || room == null) {
            // invalid; ignoring. For production return error handling.
            return;
        }

        Message msg = new Message();
        msg.setSender(sender);
        msg.setRoom(room);
        msg.setContent(dto.getContent());
        msg.setTimestamp(LocalDateTime.now());
        Message saved = messageService.save(msg);

        // broadcast to subscribers of topic for that room
        messagingTemplate.convertAndSend("/topic/rooms/" + room.getId(), saved);
    }
}
