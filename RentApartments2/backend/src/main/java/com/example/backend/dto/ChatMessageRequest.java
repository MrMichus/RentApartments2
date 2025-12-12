package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ChatMessageRequest {
    
    @NotNull(message = "ID odbiorcy jest wymagane")
    private Long recipientId;
    
    @NotBlank(message = "Treść wiadomości jest wymagana")
    private String tresc;

    public ChatMessageRequest() {}

    public Long getRecipientId() { return recipientId; }
    public void setRecipientId(Long recipientId) { this.recipientId = recipientId; }

    public String getTresc() { return tresc; }
    public void setTresc(String tresc) { this.tresc = tresc; }
}
