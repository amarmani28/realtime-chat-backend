package com.chat_app.ChatApp.repositories;
import com.chat_app.ChatApp.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Room findByRoomId(String roomId);
}