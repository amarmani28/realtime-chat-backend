package com.chat_app.ChatApp.controllers;

import com.chat_app.ChatApp.entities.Message;
import com.chat_app.ChatApp.entities.Room;
import com.chat_app.ChatApp.playload.MessageRequest;
import com.chat_app.ChatApp.repositories.RoomRepository;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.Map;

@Controller
@CrossOrigin("https://realtime-chat-frontend-f4ok.onrender.com")
public class ChatController {

    private final RoomRepository roomRepository;

    public ChatController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @MessageMapping("/sendMessage/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public Message sendMessage(
            @DestinationVariable String roomId,
            @RequestBody MessageRequest request
    ) throws RuntimeException {
        Room room = roomRepository.findByRoomId(request.getRoomId());
        Message message = new Message();
        message.setContent(request.getContent());
        message.setSender(request.getSender());
        message.setFileUrl(request.getFileUrl());
        message.setFileName(request.getFileName());
        message.setMessageType(request.getMessageType()); // TEXT or FILE
        message.setTimeStamp(LocalDateTime.now());

        if (room != null) {
            room.getMessages().add(message);
            roomRepository.save(room);
        } else {
            throw new RuntimeException("Room is not found");
        }
        return message;
    }

    //Typing Indicator
    @MessageMapping("/typing/{roomId}")
    @SendTo("/topic/typing/{roomId}")
    public Map<String, Object> handleTyping(
            @DestinationVariable String roomId,
            @Payload Map<String, Object> payload
    ) {
        return payload;
    }

    @MessageMapping("/user-event/{roomId}")
    @SendTo("/topic/user-event/{roomId}")
    public Map<String, Object> handleUserEvent(
            @DestinationVariable String roomId,
            @Payload Map<String, Object> payload
    ) {
        return payload;
    }
}