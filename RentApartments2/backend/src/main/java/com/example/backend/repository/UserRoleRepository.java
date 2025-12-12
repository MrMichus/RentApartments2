package com.example.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.model.Rola;
import com.example.backend.model.User;
import com.example.backend.model.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    
    List<UserRole> findByUser(User user);
    
    List<UserRole> findByUserId(Long userId);
    
    Optional<UserRole> findByUserAndRole(User user, Rola role);
    
    void deleteByUserAndRole(User user, Rola role);
    
    boolean existsByUserAndRole(User user, Rola role);
}
