package com.example.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.dto.AuthResponse;
import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.RegisterRequest;
import com.example.backend.dto.UserDTO;
import com.example.backend.model.Rola;
import com.example.backend.model.User;
import com.example.backend.model.UserRole;
import com.example.backend.repository.RolaRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.UserRoleRepository;
import com.example.backend.security.CustomUserDetails;
import com.example.backend.security.JwtUtils;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolaRepository rolaRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private LogService logService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email jest już zajęty");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setSurname(request.getSurname());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());

        user = userRepository.save(user);

        // Przypisz rolę USER
        Rola userRole = rolaRepository.findByRolename("USER")
                .orElseThrow(() -> new RuntimeException("Rola USER nie znaleziona"));

        UserRole ur = new UserRole();
        ur.setUser(user);
        ur.setRole(userRole);
        userRoleRepository.save(ur);

        // Generuj token
        String token = jwtUtils.generateToken(user.getEmail());

        logService.log(user.getId(), "REGISTER", "Użytkownik zarejestrował się: " + user.getEmail());

        return new AuthResponse(
                token,
                user.getEmail(),
                user.getUsername(),
                user.getSurname(),
                user.getId(),
                List.of("USER")
        );
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateToken(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.toList());

        logService.log(userDetails.getId(), "LOGIN", "Użytkownik zalogował się: " + userDetails.getEmail());

        return new AuthResponse(
                token,
                userDetails.getEmail(),
                userDetails.getName(),
                userDetails.getSurname(),
                userDetails.getId(),
                roles
        );
    }

    public UserDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Użytkownik nie znaleziony"));

        return convertToDTO(user);
    }

    public UserDTO convertToDTO(User user) {
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
