package com.example.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.backend.model.Chat;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {
    
    // Znajdź czaty gdzie użytkownik jest jednym z uczestników
    List<Chat> findByUser1IdOrUser2Id(Long user1Id, Long user2Id);
    
    // Znajdź konkretny czat między dwoma użytkownikami
    @Query("{ $or: [ { 'user1_id': ?0, 'user2_id': ?1 }, { 'user1_id': ?1, 'user2_id': ?0 } ] }")
    Optional<Chat> findChatBetweenUsers(Long userId1, Long userId2);
    
    // Znajdź czaty z samym sobą (user1_id == user2_id)
    List<Chat> findByUser1IdAndUser2Id(Long user1Id, Long user2Id);
}
