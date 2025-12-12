package com.example.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.model.Rola;
import com.example.backend.model.User;
import com.example.backend.model.UserRole;
import com.example.backend.repository.RolaRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.UserRoleRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RolaRepository rolaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.surname}")
    private String adminSurname;

    @Value("${admin.phone}")
    private String adminPhone;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Utwórz role jeśli nie istnieją
        createRoleIfNotExists("USER");
        createRoleIfNotExists("ADMIN");

        // Utwórz konto admina jeśli nie istnieje
        createAdminIfNotExists();

        System.out.println("=== Data initialization completed ===");
    }

    private void createRoleIfNotExists(String roleName) {
        if (!rolaRepository.existsByRolename(roleName)) {
            Rola role = new Rola();
            role.setRolename(roleName);
            rolaRepository.save(role);
            System.out.println("Created role: " + roleName);
        }
    }

    private void createAdminIfNotExists() {
        if (!userRepository.existsByEmail(adminEmail)) {
            // Utwórz użytkownika admin
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setSurname(adminSurname);
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setPhoneNumber(adminPhone);
            admin = userRepository.save(admin);

            // Przypisz rolę ADMIN
            Rola adminRole = rolaRepository.findByRolename("ADMIN")
                    .orElseThrow(() -> new RuntimeException("Rola ADMIN nie znaleziona"));

            UserRole userRole = new UserRole();
            userRole.setUser(admin);
            userRole.setRole(adminRole);
            userRoleRepository.save(userRole);

            // Przypisz również rolę USER (admin może robić wszystko co user)
            Rola userRoleEntity = rolaRepository.findByRolename("USER")
                    .orElseThrow(() -> new RuntimeException("Rola USER nie znaleziona"));

            UserRole userRoleUser = new UserRole();
            userRoleUser.setUser(admin);
            userRoleUser.setRole(userRoleEntity);
            userRoleRepository.save(userRoleUser);

            System.out.println("Created admin user: " + adminEmail);
        } else {
            System.out.println("Admin user already exists: " + adminEmail);
        }
    }
}
