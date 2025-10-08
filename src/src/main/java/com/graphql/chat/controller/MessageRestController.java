package com.graphql.chat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import com.graphql.chat.dto.MessageDTO;
import com.graphql.chat.model.ChatRoom;
import com.graphql.chat.model.Message;
import com.graphql.chat.model.User;
import com.graphql.chat.service.ChatRoomService;
import com.graphql.chat.service.MessageService;
import com.graphql.chat.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageRestController {
    private final MessageService messageService;
    private final UserService userService;
    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageRestController(MessageService messageService,
                                 UserService userService,
                                 ChatRoomService chatRoomService,
                                 SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.userService = userService;
        this.chatRoomService = chatRoomService;
        this.messagingTemplate = messagingTemplate;
    }

    // send message (persist + broadcast to websocket topic)
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDTO dto) {
        User sender = userService.findById(dto.getSenderId()).orElse(null);
        ChatRoom room = chatRoomService.findById(dto.getRoomId()).orElse(null);
        if (sender == null || room == null) {
            return ResponseEntity.badRequest().body("Invalid senderId or roomId");
        }

        Message msg = new Message();
        msg.setSender(sender);
        msg.setRoom(room);
        msg.setContent(dto.getContent());
        msg.setTimestamp(LocalDateTime.now());
        Message saved = messageService.save(msg);

        // broadcast to /topic/rooms/{roomId}
        messagingTemplate.convertAndSend("/topic/rooms/" + room.getId(), saved);

        return ResponseEntity.ok(saved);
    }

    // fetch history for a room
    @GetMapping("/room/{roomId}")
    public ResponseEntity<?> getRoomMessages(@PathVariable Long roomId) {
        ChatRoom room = chatRoomService.findById(roomId).orElse(null);
        if (room == null) return ResponseEntity.notFound().build();
        List<Message> msgs = messageService.getMessagesForRoom(room);
        return ResponseEntity.ok(msgs);
    }
}
