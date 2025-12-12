package com.example.backend.dto;

import java.util.List;

public class AuthResponse {
    
    private String token;
    private String email;
    private String username;
    private String surname;
    private Long userId;
    private List<String> roles;

    public AuthResponse() {}

    public AuthResponse(String token, String email, String username, String surname, Long userId, List<String> roles) {
        this.token = token;
        this.email = email;
        this.username = username;
        this.surname = surname;
        this.userId = userId;
        this.roles = roles;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
}
