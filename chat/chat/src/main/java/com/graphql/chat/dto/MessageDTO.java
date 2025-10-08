package com.graphql.chat.dto;

public class MessageDTO {
    private Long senderId;
    private Long roomId;
    private String content;

    public MessageDTO() {}

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }
    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
