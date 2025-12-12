package com.example.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.model.Rola;

@Repository
public interface RolaRepository extends JpaRepository<Rola, Long> {
    
    Optional<Rola> findByRolename(String rolename);
    
    boolean existsByRolename(String rolename);
}
