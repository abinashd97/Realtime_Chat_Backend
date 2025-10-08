package com.graphql.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.graphql.chat.model.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}

