package com.example.backend.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "logs")
public class Log {

    @Id
    private String id;

    @Field("user_id")
    private Long userId;

    @Field("akcja")
    private String akcja;

    @Field("timestamp")
    private LocalDateTime timestamp;

    @Field("opis")
    private String opis;

    public Log() {}

    public Log(Long userId, String akcja, String opis) {
        this.userId = userId;
        this.akcja = akcja;
        this.opis = opis;
        this.timestamp = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getAkcja() { return akcja; }
    public void setAkcja(String akcja) { this.akcja = akcja; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getOpis() { return opis; }
    public void setOpis(String opis) { this.opis = opis; }
}
