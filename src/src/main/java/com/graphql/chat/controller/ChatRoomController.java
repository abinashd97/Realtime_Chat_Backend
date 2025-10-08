package com.graphql.chat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.graphql.chat.model.ChatRoom;
import com.graphql.chat.service.ChatRoomService;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class ChatRoomController {
    private final ChatRoomService svc;
    public ChatRoomController(ChatRoomService svc) { this.svc = svc; }

    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody ChatRoom room) {
        ChatRoom created = svc.createRoom(room);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<ChatRoom>> listRooms() {
        return ResponseEntity.ok(svc.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoom(@PathVariable Long id) {
        return svc.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
