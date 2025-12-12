package com.example.backend.dto;

import java.util.List;

public class UserDTO {
    
    private Long id;
    private String username;
    private String surname;
    private String email;
    private String phoneNumber;
    private List<String> roles;

    public UserDTO() {}

    public UserDTO(Long id, String username, String surname, String email, String phoneNumber, List<String> roles) {
        this.id = id;
        this.username = username;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.roles = roles;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
}
