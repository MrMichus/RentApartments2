package com.example.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.dto.UpdateProfileRequest;
import com.example.backend.dto.UserDTO;
import com.example.backend.model.Rola;
import com.example.backend.model.User;
import com.example.backend.model.UserRole;
import com.example.backend.repository.RolaRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.UserRoleRepository;
import com.example.backend.security.CustomUserDetails;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolaRepository rolaRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private LogService logService;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie znaleziony"));
        return convertToDTO(user);
    }

    public User getCurrentUser() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Użytkownik nie znaleziony"));
    }

    public Long getCurrentUserId() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userDetails.getId();
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie znaleziony"));
        
        logService.log(getCurrentUserId(), "DELETE_USER", "Usunięto użytkownika: " + user.getEmail());
        userRepository.delete(user);
    }

    @Transactional
    public UserDTO changeUserRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie znaleziony"));

        Rola newRole = rolaRepository.findByRolename(roleName)
                .orElseThrow(() -> new RuntimeException("Rola nie znaleziona: " + roleName));

        // Usuń wszystkie obecne role
        userRoleRepository.deleteAll(user.getRoles());
        user.getRoles().clear();

        // Dodaj nową rolę
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(newRole);
        userRoleRepository.save(userRole);

        logService.log(getCurrentUserId(), "CHANGE_ROLE", 
                "Zmieniono rolę użytkownika " + user.getEmail() + " na " + roleName);

        // Odśwież użytkownika
        user = userRepository.findById(userId).get();
        return convertToDTO(user);
    }

    @Transactional
    public UserDTO updateProfile(UpdateProfileRequest request) {
        User user = getCurrentUser();
        
        user.setUsername(request.getUsername());
        user.setSurname(request.getSurname());
        user.setPhoneNumber(request.getPhoneNumber());
        
        userRepository.save(user);
        
        logService.log(user.getId(), "UPDATE_PROFILE", "Zaktualizowano profil użytkownika");
        
        return convertToDTO(user);
    }

    private UserDTO convertToDTO(User user) {
        List<String> roles = user.getRoles().stream()
                .map(ur -> ur.getRole().getRolename())
                .collect(Collectors.toList());

        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getSurname(),
                user.getEmail(),
                user.getPhoneNumber(),
                roles
        );
    }
}
