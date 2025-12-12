package com.example.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.model.Adres;

@Repository
public interface AdresRepository extends JpaRepository<Adres, Long> {
    
    List<Adres> findByMiasto(String miasto);
    
    Optional<Adres> findByUlicaAndNumerAndKodPocztowyAndMiasto(
            String ulica, String numer, String kodPocztowy, String miasto);
}
