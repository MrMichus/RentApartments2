package com.example.backend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.dto.ChatDTO;
import com.example.backend.dto.ChatMessageRequest;
import com.example.backend.model.Chat;
import com.example.backend.model.User;
import com.example.backend.repository.ChatRepository;
import com.example.backend.repository.UserRepository;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;

    public List<ChatDTO> getMyChats() {
        User currentUser = userService.getCurrentUser();
        
        // Usuń ewentualne stare czaty z samym sobą (błędne dane)
        List<Chat> selfChats = chatRepository.findByUser1IdAndUser2Id(currentUser.getId(), currentUser.getId());
        if (!selfChats.isEmpty()) {
            chatRepository.deleteAll(selfChats);
        }
        
        // Pobierz czaty gdzie użytkownik jest uczestnikiem
        List<Chat> chats = chatRepository.findByUser1IdOrUser2Id(
                currentUser.getId(), currentUser.getId());
        
        // Filtruj czaty z samym sobą (na wszelki wypadek)
        return chats.stream()
                .filter(chat -> !chat.getUser1Id().equals(chat.getUser2Id()))
                .map(chat -> convertToDTO(chat, currentUser.getId()))
                .collect(Collectors.toList());
    }

    public ChatDTO getOrCreateChat(Long recipientId) {
        User currentUser = userService.getCurrentUser();
        
        // Sprawdź czy użytkownik nie próbuje rozpocząć czatu z samym sobą
        if (currentUser.getId().equals(recipientId)) {
            throw new RuntimeException("Nie możesz rozpocząć czatu z samym sobą");
        }
        
        // Sprawdź czy odbiorca istnieje
        userRepository.findById(recipientId)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie znaleziony"));
        
        // Szukamy istniejącego czatu między użytkownikami
        Optional<Chat> existingChat = chatRepository.findChatBetweenUsers(
                currentUser.getId(), recipientId);
        
        if (existingChat.isPresent()) {
            return convertToDTO(existingChat.get(), currentUser.getId());
        }
        
        // Twórz nowy czat
        Chat chat = new Chat(currentUser.getId(), recipientId);
        chat = chatRepository.save(chat);
        
        return convertToDTO(chat, currentUser.getId());
    }

    public ChatDTO sendMessage(ChatMessageRequest request) {
        User currentUser = userService.getCurrentUser();
        
        // Sprawdź czy użytkownik nie próbuje wysłać wiadomości do siebie
        if (currentUser.getId().equals(request.getRecipientId())) {
            throw new RuntimeException("Nie możesz wysłać wiadomości do siebie");
        }
        
        // Sprawdź czy odbiorca istnieje
        userRepository.findById(request.getRecipientId())
                .orElseThrow(() -> new RuntimeException("Użytkownik nie znaleziony"));
        
        // Znajdź lub utwórz czat
        Optional<Chat> existingChat = chatRepository.findChatBetweenUsers(
                currentUser.getId(), request.getRecipientId());
        
        Chat chat;
        if (existingChat.isPresent()) {
            chat = existingChat.get();
        } else {
            chat = new Chat(currentUser.getId(), request.getRecipientId());
        }
        
        // Dodaj wiadomość
        Chat.Wiadomosc wiadomosc = new Chat.Wiadomosc(
                currentUser.getId(),
                request.getTresc(),
                LocalDateTime.now()
        );
        
        if (chat.getWiadomosci() == null) {
            chat.setWiadomosci(new ArrayList<>());
        }
        chat.getWiadomosci().add(wiadomosc);
        chat.setUpdatedAt(LocalDateTime.now());
        
        chat = chatRepository.save(chat);
        
        logService.log(currentUser.getId(), "SEND_MESSAGE", 
                "Wysłano wiadomość do użytkownika ID: " + request.getRecipientId());
        
        return convertToDTO(chat, currentUser.getId());
    }

    public ChatDTO getChatById(String chatId) {
        User currentUser = userService.getCurrentUser();
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Czat nie znaleziony"));
        
        // Sprawdź czy użytkownik jest uczestnikiem czatu
        if (!chat.getUser1Id().equals(currentUser.getId()) && 
            !chat.getUser2Id().equals(currentUser.getId())) {
            throw new RuntimeException("Brak dostępu do tego czatu");
        }
        
        return convertToDTO(chat, currentUser.getId());
    }

    private ChatDTO convertToDTO(Chat chat, Long currentUserId) {
        ChatDTO dto = new ChatDTO();
        dto.setId(chat.getId());
        dto.setCreatedAt(chat.getCreatedAt());
        dto.setUpdatedAt(chat.getUpdatedAt());
        
        // Ustal kto jest nadawcą a kto odbiorcą z perspektywy bieżącego użytkownika
        Long otherUserId;
        if (chat.getUser1Id().equals(currentUserId)) {
            dto.setSenderId(chat.getUser1Id());
            dto.setRecipientId(chat.getUser2Id());
            otherUserId = chat.getUser2Id();
        } else {
            dto.setSenderId(chat.getUser2Id());
            dto.setRecipientId(chat.getUser1Id());
            otherUserId = chat.getUser1Id();
        }
        
        // Pobierz nazwy użytkowników
        userRepository.findById(currentUserId)
                .ifPresent(user -> dto.setSenderName(user.getUsername() + " " + user.getSurname()));
        
        userRepository.findById(otherUserId)
                .ifPresent(user -> dto.setRecipientName(user.getUsername() + " " + user.getSurname()));
        
        // Konwertuj wiadomości
        if (chat.getWiadomosci() != null) {
            List<ChatDTO.MessageDTO> messageDTOs = chat.getWiadomosci().stream()
                    .map(msg -> {
                        ChatDTO.MessageDTO msgDTO = new ChatDTO.MessageDTO();
                        msgDTO.setUserId(msg.getSenderId());
                        msgDTO.setTresc(msg.getTresc());
                        msgDTO.setTimestamp(msg.getTimestamp());
                        
                        userRepository.findById(msg.getSenderId())
                                .ifPresent(user -> msgDTO.setUserName(user.getUsername() + " " + user.getSurname()));
                        
                        return msgDTO;
                    })
                    .collect(Collectors.toList());
            dto.setWiadomosci(messageDTOs);
        }
        
        return dto;
    }
}
