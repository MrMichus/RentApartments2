package com.example.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.ChatDTO;
import com.example.backend.dto.ChatMessageRequest;
import com.example.backend.service.ChatService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class ChatController {

    @Autowired
    private ChatService chatService;

    // Pobierz moje czaty
    @GetMapping
    public ResponseEntity<List<ChatDTO>> getMyChats() {
        return ResponseEntity.ok(chatService.getMyChats());
    }

    // Pobierz lub utwórz czat z użytkownikiem
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getOrCreateChat(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(chatService.getOrCreateChat(userId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }

    // Pobierz konkretny czat
    @GetMapping("/{chatId}")
    public ResponseEntity<ChatDTO> getChatById(@PathVariable String chatId) {
        try {
            return ResponseEntity.ok(chatService.getChatById(chatId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Wyślij wiadomość
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@Valid @RequestBody ChatMessageRequest request) {
        try {
            return ResponseEntity.ok(chatService.sendMessage(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }
}
