package com.graphql.chat.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="sender_id")
    private User sender;

    @Column(nullable=false)
    private String content;

    @Column(nullable=false)
    private LocalDateTime timestamp;

    // link to chat room
    @ManyToOne(optional=false)
    @JoinColumn(name="room_id")
    private ChatRoom room;

    public Message() {}

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getSender() { return sender; }
    public void setSender(User sender) { this.sender = sender; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public ChatRoom getRoom() { return room; }
    public void setRoom(ChatRoom room) { this.room = room; }
}
