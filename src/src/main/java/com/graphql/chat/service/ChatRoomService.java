package com.graphql.chat.service;

import org.springframework.stereotype.Service;

import com.graphql.chat.model.ChatRoom;
import com.graphql.chat.repository.ChatRoomRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ChatRoomService {
    private final ChatRoomRepository repo;
    public ChatRoomService(ChatRoomRepository repo) { this.repo = repo; }

    public ChatRoom createRoom(ChatRoom room) { return repo.save(room); }
    public Optional<ChatRoom> findById(Long id) { return repo.findById(id); }
    public List<ChatRoom> findAll() { return repo.findAll(); }
}

