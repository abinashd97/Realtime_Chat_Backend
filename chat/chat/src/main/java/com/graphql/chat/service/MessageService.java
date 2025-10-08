package com.graphql.chat.service;

import org.springframework.stereotype.Service;

import com.graphql.chat.model.ChatRoom;
import com.graphql.chat.model.Message;
import com.graphql.chat.repository.MessageRepository;

import java.util.List;

@Service
public class MessageService {
    private final MessageRepository repo;
    public MessageService(MessageRepository repo) { this.repo = repo; }

    public Message save(Message message) {
        return repo.save(message);
    }

    public List<Message> getMessagesForRoom(ChatRoom room) {
        return repo.findByRoomOrderByTimestampAsc(room);
    }
}


