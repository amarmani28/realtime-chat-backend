package com.chat_app.ChatApp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime timeStamp;

    private String fileUrl;

    private String fileName;

    private String messageType;

    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
        this.messageType = "TEXT";
        this.timeStamp = LocalDateTime.now();
    }
}