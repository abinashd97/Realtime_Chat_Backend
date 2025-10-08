package com.graphql.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.graphql.chat.model.ChatRoom;
import com.graphql.chat.model.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByRoomOrderByTimestampAsc(ChatRoom room);
}

