package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.backend.model.Mieszkanie;
import com.example.backend.model.User;

@Repository
public interface MieszkanieRepository extends JpaRepository<Mieszkanie, Long>, JpaSpecificationExecutor<Mieszkanie> {
    
    List<Mieszkanie> findByOwner(User owner);
    
    List<Mieszkanie> findByOwnerId(Long ownerId);
    
    List<Mieszkanie> findByStatus(String status);
    
    @Query("SELECT m FROM Mieszkanie m WHERE m.status = 'APPROVED'")
    List<Mieszkanie> findAllApproved();
    
    @Query("SELECT m FROM Mieszkanie m WHERE m.status = 'PENDING'")
    List<Mieszkanie> findAllPending();
    
    @Query("SELECT m FROM Mieszkanie m WHERE m.status = 'APPROVED' AND " +
           "(:minPrice IS NULL OR m.cena_miesieczna >= :minPrice) AND " +
           "(:maxPrice IS NULL OR m.cena_miesieczna <= :maxPrice) AND " +
           "(:miasto IS NULL OR LOWER(m.adres.miasto) LIKE LOWER(CONCAT('%', :miasto, '%')))")
    List<Mieszkanie> findByFilters(@Param("minPrice") Double minPrice, 
                                    @Param("maxPrice") Double maxPrice,
                                    @Param("miasto") String miasto);
    
    @Query("SELECT m FROM Mieszkanie m WHERE m.status = 'APPROVED' AND " +
           "(LOWER(m.tytul) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(m.opis) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(m.adres.miasto) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Mieszkanie> search(@Param("query") String query);
}
