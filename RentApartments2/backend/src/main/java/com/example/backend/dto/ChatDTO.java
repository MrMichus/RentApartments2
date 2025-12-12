package com.example.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ChatDTO {
    
    private String id;
    private Long senderId;
    private String senderName;
    private Long recipientId;
    private String recipientName;
    private List<MessageDTO> wiadomosci;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ChatDTO() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public Long getRecipientId() { return recipientId; }
    public void setRecipientId(Long recipientId) { this.recipientId = recipientId; }

    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }

    public List<MessageDTO> getWiadomosci() { return wiadomosci; }
    public void setWiadomosci(List<MessageDTO> wiadomosci) { this.wiadomosci = wiadomosci; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static class MessageDTO {
        private Long userId;
        private String userName;
        private String tresc;
        private LocalDateTime timestamp;

        public MessageDTO() {}

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }

        public String getTresc() { return tresc; }
        public void setTresc(String tresc) { this.tresc = tresc; }

        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    }
}
