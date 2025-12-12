package com.example.backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.model.Zdjecie;

@Repository
public interface ZdjecieRepository extends MongoRepository<Zdjecie, String> {
    
    List<Zdjecie> findByMieszkanieId(Long mieszkanieId);
    
    void deleteByMieszkanieId(Long mieszkanieId);
}
