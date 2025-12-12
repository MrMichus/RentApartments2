package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class RoleChangeRequest {
    
    @NotBlank(message = "Nazwa roli jest wymagana")
    private String roleName;

    public RoleChangeRequest() {}

    public RoleChangeRequest(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
}
