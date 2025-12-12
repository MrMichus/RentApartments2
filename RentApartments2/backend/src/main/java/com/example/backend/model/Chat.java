package com.example.backend.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "chats")
public class Chat {

  @Id
  private String id;

  // ID pierwszego użytkownika w konwersacji
  @Field("user1_id")
  private Long user1Id;

  // ID drugiego użytkownika w konwersacji
  @Field("user2_id")
  private Long user2Id;

  @Field("wiadomosci")
  private List<Wiadomosc> wiadomosci;

  @Field("created_at")
  private LocalDateTime createdAt;

  @Field("updated_at")
  private LocalDateTime updatedAt;

  public Chat(){}

  public Chat(Long user1Id, Long user2Id) {
    this.user1Id = user1Id;
    this.user2Id = user2Id;
    this.wiadomosci = new ArrayList<>();
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public Long getUser1Id() { return user1Id; }
  public void setUser1Id(Long user1Id) { this.user1Id = user1Id; }

  public Long getUser2Id() { return user2Id; }
  public void setUser2Id(Long user2Id) { this.user2Id = user2Id; }

  public List<Wiadomosc> getWiadomosci() { return wiadomosci; }
  public void setWiadomosci(List<Wiadomosc> wiadomosci) { this.wiadomosci = wiadomosci; }

  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

  public LocalDateTime getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

  public static class Wiadomosc {
    @Field("sender_id")
    private Long senderId;
    
    @Field("tresc")
    private String tresc;
    
    @Field("timestamp")
    private LocalDateTime timestamp;

    public Wiadomosc() {}
    
    public Wiadomosc(Long senderId, String tresc, LocalDateTime timestamp) {
      this.senderId = senderId;
      this.tresc = tresc;
      this.timestamp = timestamp;
    }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public String getTresc() { return tresc; }
    public void setTresc(String tresc) { this.tresc = tresc; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
  }
}
