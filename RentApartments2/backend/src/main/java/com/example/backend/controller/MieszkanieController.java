package com.example.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.ApiResponse;
import com.example.backend.dto.MieszkanieDTO;
import com.example.backend.dto.MieszkanieRequest;
import com.example.backend.service.MieszkanieService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/mieszkania")
@CrossOrigin
public class MieszkanieController {

    @Autowired
    private MieszkanieService mieszkanieService;

    // Publiczne - lista zatwierdzonych mieszkań
    @GetMapping
    public ResponseEntity<List<MieszkanieDTO>> getAllApproved() {
        return ResponseEntity.ok(mieszkanieService.getAllApproved());
    }

    // Publiczne - szczegóły mieszkania
    @GetMapping("/{id}")
    public ResponseEntity<MieszkanieDTO> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(mieszkanieService.getById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Publiczne - wyszukiwanie
    @GetMapping("/search")
    public ResponseEntity<List<MieszkanieDTO>> search(@RequestParam String query) {
        return ResponseEntity.ok(mieszkanieService.search(query));
    }

    // Publiczne - filtrowanie
    @GetMapping("/filter")
    public ResponseEntity<List<MieszkanieDTO>> filter(
            @RequestParam(required = false) Double cenaMin,
            @RequestParam(required = false) Double cenaMax,
            @RequestParam(required = false) String miasto,
            @RequestParam(required = false) Integer liczbaPokoi) {
        return ResponseEntity.ok(mieszkanieService.filter(cenaMin, cenaMax, miasto, liczbaPokoi));
    }

    // Wymagane logowanie - moje mieszkania
    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MieszkanieDTO>> getMyMieszkania() {
        return ResponseEntity.ok(mieszkanieService.getMyMieszkania());
    }

    // Wymagane logowanie - dodaj mieszkanie
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MieszkanieDTO> create(@Valid @RequestBody MieszkanieRequest request) {
        try {
            return ResponseEntity.ok(mieszkanieService.create(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Wymagane logowanie - edytuj mieszkanie
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MieszkanieDTO> update(@PathVariable Long id, 
                                                 @Valid @RequestBody MieszkanieRequest request) {
        try {
            return ResponseEntity.ok(mieszkanieService.update(id, request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Wymagane logowanie - usuń mieszkanie
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        try {
            mieszkanieService.delete(id);
            return ResponseEntity.ok(ApiResponse.success("Mieszkanie zostało usunięte"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
