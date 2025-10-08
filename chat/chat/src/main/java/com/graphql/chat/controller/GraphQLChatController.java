package com.graphql.chat.controller;

import org.reactivestreams.Publisher;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.graphql.chat.model.ChatRoom;
import com.graphql.chat.model.Message;
import com.graphql.chat.model.User;
import com.graphql.chat.service.ChatRoomService;
import com.graphql.chat.service.MessageService;
import com.graphql.chat.service.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class GraphQLChatController {

    private final UserService userService;
    private final ChatRoomService chatRoomService;
    private final MessageService messageService;

    // For subscriptions
    private final Sinks.Many<Message> messageSink = Sinks.many().multicast().onBackpressureBuffer();

    public GraphQLChatController(UserService userService,
                                 ChatRoomService chatRoomService,
                                 MessageService messageService) {
        this.userService = userService;
        this.chatRoomService = chatRoomService;
        this.messageService = messageService;
    }

    // === QUERIES ===
    @QueryMapping
    public List<User> users() {
        return userService.findAll();
    }

    @QueryMapping
    public List<ChatRoom> rooms() {
        return chatRoomService.findAll();
    }

    @QueryMapping
    public List<Message> messagesByRoom(@Argument String roomId) {
        Long roomIdLong = Long.parseLong(roomId);
        ChatRoom room = chatRoomService.findById(roomIdLong).orElseThrow();
        return messageService.getMessagesForRoom(room);
    }

    // === MUTATIONS ===
    @MutationMapping
    public User createUser(@Argument String username, @Argument String displayName) {
        User user = new User();
        user.setUsername(username);
        user.setDisplayName(displayName);
        return userService.createUser(user);
    }

    @MutationMapping
    public ChatRoom createRoom(@Argument String name) {
        ChatRoom room = new ChatRoom();
        room.setName(name);
        return chatRoomService.createRoom(room);
    }

    @MutationMapping
    public Message sendMessage(@Argument String senderId, @Argument String roomId, @Argument String content) {
        Long senderIdLong = Long.parseLong(senderId);
        Long roomIdLong = Long.parseLong(roomId);
        
        User sender = userService.findById(senderIdLong).orElseThrow(() -> new RuntimeException("User not found"));
        ChatRoom room = chatRoomService.findById(roomIdLong).orElseThrow(() -> new RuntimeException("Room not found"));

        Message msg = new Message();
        msg.setSender(sender);
        msg.setRoom(room);
        msg.setContent(content);
        msg.setTimestamp(LocalDateTime.now());

        Message saved = messageService.save(msg);
        messageSink.tryEmitNext(saved); // notify subscribers
        return saved;
    }

    // === SUBSCRIPTION ===
    @SubscriptionMapping
    public Publisher<Message> messageAdded(@Argument String roomId) {
        Long roomIdLong = Long.parseLong(roomId);
        return messageSink.asFlux()
                .filter(msg -> msg.getRoom().getId().equals(roomIdLong));
    }
}

