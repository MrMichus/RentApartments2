package com.example.backend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.model.Log;

@Repository
public interface LogRepository extends MongoRepository<Log, String> {
    
    List<Log> findByUserId(Long userId);
    
    List<Log> findByAkcja(String akcja);
    
    List<Log> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    
    List<Log> findAllByOrderByTimestampDesc();
}
