package com.chat_app.ChatApp.controllers;

import com.chat_app.ChatApp.entities.Room;
import com.chat_app.ChatApp.repositories.RoomRepository;
import com.chat_app.ChatApp.entities.Message;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/rooms")
@CrossOrigin("https://realtime-chat-frontend-f4ok.onrender.com")
public class RoomController {

    private RoomRepository roomRepository;

    public RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

@PostMapping
public ResponseEntity<?> createRoom(@RequestBody Map<String, String> request) {
    String roomId = request.get("roomId");

    if (roomId == null || roomId.trim().isEmpty()) {
        return ResponseEntity.badRequest().body("Invalid Room ID");
    }

    if (roomRepository.findByRoomId(roomId) != null) {
        return ResponseEntity.badRequest().body("Room already exists");
    }

    Room room = new Room();
    room.setRoomId(roomId);
    Room savedRoom = roomRepository.save(room);

    return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
}

    @GetMapping("/{roomId}")
    public ResponseEntity<?> joinRoom(
            @PathVariable String roomId
    ) {

        Room room = roomRepository.findByRoomId(roomId);

        if (room == null) {
            return ResponseEntity.badRequest().body("Room not found");
        }

        return ResponseEntity.ok(room);
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<Message>> getMessage(
            @PathVariable String roomId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {

        Room room = roomRepository.findByRoomId(roomId);

        if (room == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Message> messages = room.getMessages();

        int start = Math.max(0, messages.size() - (page + 1) * size);
        int end = Math.min(messages.size(), start + size);

        List<Message> paginatedMessage = messages.subList(start, end);

        return ResponseEntity.ok(paginatedMessage);
    }
}
