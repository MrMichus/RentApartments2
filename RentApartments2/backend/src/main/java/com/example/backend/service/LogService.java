package com.example.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.model.Log;
import com.example.backend.repository.LogRepository;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public void log(Long userId, String akcja, String opis) {
        Log log = new Log(userId, akcja, opis);
        logRepository.save(log);
    }

    public List<Log> getAllLogs() {
        return logRepository.findAllByOrderByTimestampDesc();
    }

    public List<Log> getLogsByUser(Long userId) {
        return logRepository.findByUserId(userId);
    }

    public List<Log> getLogsByAction(String akcja) {
        return logRepository.findByAkcja(akcja);
    }
}
