package com.example.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.ApiResponse;
import com.example.backend.dto.MieszkanieDTO;
import com.example.backend.dto.MieszkanieRequest;
import com.example.backend.dto.RoleChangeRequest;
import com.example.backend.dto.UserDTO;
import com.example.backend.model.Log;
import com.example.backend.service.LogService;
import com.example.backend.service.MieszkanieService;
import com.example.backend.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private MieszkanieService mieszkanieService;

    @Autowired
    private LogService logService;

    // ===== ZARZĄDZANIE UŻYTKOWNIKAMI =====

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(ApiResponse.success("Użytkownik został usunięty"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<UserDTO> changeUserRole(@PathVariable Long id, 
                                                   @Valid @RequestBody RoleChangeRequest request) {
        try {
            return ResponseEntity.ok(userService.changeUserRole(id, request.getRoleName()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // ===== ZARZĄDZANIE MIESZKANIAMI =====

    @GetMapping("/mieszkania")
    public ResponseEntity<List<MieszkanieDTO>> getAllMieszkania() {
        return ResponseEntity.ok(mieszkanieService.getAll());
    }

    @GetMapping("/mieszkania/pending")
    public ResponseEntity<List<MieszkanieDTO>> getPendingMieszkania() {
        return ResponseEntity.ok(mieszkanieService.getAllPending());
    }

    @PutMapping("/mieszkania/{id}/approve")
    public ResponseEntity<MieszkanieDTO> approveMieszkanie(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(mieszkanieService.approve(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/mieszkania/{id}/reject")
    public ResponseEntity<MieszkanieDTO> rejectMieszkanie(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(mieszkanieService.reject(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/mieszkania/{id}")
    public ResponseEntity<MieszkanieDTO> updateMieszkanie(@PathVariable Long id, 
                                                          @Valid @RequestBody MieszkanieRequest request) {
        try {
            return ResponseEntity.ok(mieszkanieService.update(id, request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/mieszkania/{id}")
    public ResponseEntity<ApiResponse> deleteMieszkanie(@PathVariable Long id) {
        try {
            mieszkanieService.delete(id);
            return ResponseEntity.ok(ApiResponse.success("Mieszkanie zostało usunięte"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // ===== LOGI =====

    @GetMapping("/logs")
    public ResponseEntity<List<Log>> getAllLogs() {
        return ResponseEntity.ok(logService.getAllLogs());
    }

    @GetMapping("/logs/user/{userId}")
    public ResponseEntity<List<Log>> getLogsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(logService.getLogsByUser(userId));
    }

    @GetMapping("/logs/action/{akcja}")
    public ResponseEntity<List<Log>> getLogsByAction(@PathVariable String akcja) {
        return ResponseEntity.ok(logService.getLogsByAction(akcja));
    }
}
